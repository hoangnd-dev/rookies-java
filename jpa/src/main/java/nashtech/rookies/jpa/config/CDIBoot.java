package nashtech.rookies.jpa.config;

import java.util.Map;
import java.util.function.Function;

import javax.sql.DataSource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.environment.se.events.ContainerShutdown;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.ValidationMode;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CDIBoot {

    public static final class TransactionManager {
        private final EntityManagerFactory       emf;
        private final ThreadLocal<EntityManager> ems = new ThreadLocal<>();

        private Pair<EntityManager, Boolean> em () {
            boolean isNew = false;
            if ( ems.get() == null ) {
                ems.set(emf.createEntityManager());
                isNew = true;
            }
            return Pair.of(ems.get(), isNew);
        }

        public TransactionManager (EntityManagerFactory entityManagerFactory) {
            this.emf = entityManagerFactory;
        }

        private void startTransaction (EntityManager entityManager) {
            if ( !entityManager.getTransaction().isActive() ) {
                entityManager.getTransaction().begin();
            }
        }

        // Helper method to commit the transaction
        private void commitTransaction (EntityManager entityManager) {
            if ( entityManager.getTransaction().isActive() ) {
                entityManager.getTransaction().commit();
            }
        }

        // Helper method to rollback the transaction
        private void rollbackTransaction (EntityManager entityManager) {
            if ( entityManager.getTransaction().isActive() ) {
                entityManager.getTransaction().rollback();
            }
        }

        // Helper method to close the entity manager
        private void closeEntityManager (EntityManager entityManager) {
            if ( entityManager.isOpen() ) {
                entityManager.close();
            }
        }

        public <R> R query (Function<EntityManager, R> func) {
            var em = em().getFirst();
            try (em) {
                return func.apply(em);
            }
            finally {
                ems.remove();
            }

        }

        public <R> R execute (Function<EntityManager, R> func) {
            var emPro         = em();
            var entityManager = emPro.getFirst();
            var isNew         = emPro.getSecond();

            try (entityManager) {
                startTransaction(entityManager);
                var r = func.apply(entityManager);
                if ( isNew ) {
                    commitTransaction(entityManager);
                }
                return r;
            }
            catch (RuntimeException e) {
                rollbackTransaction(entityManager);
                throw e;
            }
            finally {
                if ( isNew ) {
                    closeEntityManager(entityManager);
                    ems.remove();
                }
            }
        }
    }

    static class CDIConfig {

        CDIConfig () {
        }

        @Produces
        Dotenv dotenv () {
            return Dotenv.load();
        }

        @Produces
        DataSource dataSource (Dotenv dotenv) {
            var config = new HikariConfig();
            config.setJdbcUrl(dotenv.get("datasource.url"));
            config.setUsername(dotenv.get("datasource.username"));
            config.setPassword(dotenv.get("datasource.password"));
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            return new HikariDataSource(config);
        }

        @Produces
        JdbcTemplate jdbcTemplate (DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }

        @Produces
        EntityManagerFactory entityManagerFactory (DataSource dataSource, Dotenv dotenv) {
            var properties = Map.of(
                "jakarta.persistence.nonJtaDataSource", dataSource,
                "jakarta.persistence.validation.mode", ValidationMode.CALLBACK.name()
            );
            return Persistence.createEntityManagerFactory(dotenv.get("JPA_UNIT"), properties);
        }

        @Produces
        TransactionManager transactionManager (EntityManagerFactory entityManagerFactory) {
            return new TransactionManager(entityManagerFactory);
        }

        @Produces
        @Default
        EntityManager entityManager (EntityManagerFactory factory) {
            return factory.createEntityManager();
        }


        void stop (@Observes ContainerShutdown event,
                   EntityManagerFactory entityManagerFactory,
                   DataSource dataSource
        ) {
            entityManagerFactory.close();
            if ( dataSource instanceof HikariDataSource hDs ) {
                hDs.close();
            }

        }
    }

    static class MadeUpInterface implements MethodInterceptor {

        private final WeldContainer container;

        MadeUpInterface (WeldContainer container) {
            this.container = container;
        }

        void close () {
            this.container.select(EntityManagerFactory.class).get().close();
            var ds = (HikariDataSource) this.container.select(DataSource.class).get();
            ds.close();
        }

        @Override
        public Object invoke (MethodInvocation invocation) throws Throwable {
            if ( "run".equals(invocation.getMethod().getName()) ) {
                log.info("APP Startup");
            }
            var o = invocation.getMethod().invoke(invocation.getThis(), invocation.getArguments());
            if ( "run".equals(invocation.getMethod().getName()) ) {
                this.close();
                container.close();
                log.info("APP Shutdown");
            }
            return o;

        }
    }

    public static <T> T with (Class<T> appClass) {
        var instance  = SeContainerInitializer.newInstance();
        var container = instance.addBeanClasses(appClass, CDIConfig.class).initialize();
        var app       = container.select(appClass).get();
        var factory   = new ProxyFactory(app);
        factory.addAdvice(new MadeUpInterface((WeldContainer) container));
        return (T) factory.getProxy();
    }

}
