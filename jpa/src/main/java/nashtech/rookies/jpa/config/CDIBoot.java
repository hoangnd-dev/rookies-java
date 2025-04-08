package nashtech.rookies.jpa.config;

import java.util.Map;
import java.util.function.Function;

import javax.sql.DataSource;

import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.ValidationMode;
import lombok.SneakyThrows;

public class CDIBoot {

    public static final class TransactionManager {
        private final EntityManagerFactory       emf;
        private final ThreadLocal<EntityManager> ems = new InheritableThreadLocal<>();

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
            return func.apply(em().getFirst());
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

        @PreDestroy
        public void close () {
            if ( this.emf.isOpen() ) {
                this.emf.close();
            }
        }
    }

    static class CDIConfig {
        private EntityManagerFactory entityManagerFactory;
        private DataSource           dataSource;

        CDIConfig () {
            Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
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
            this.dataSource = new HikariDataSource(config);
            return this.dataSource;
        }

        @Produces
        public JdbcTemplate jdbcTemplate (DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }

        @Produces
        EntityManagerFactory entityManagerFactory (DataSource dataSource) {
            var properties = Map.of(
                "jakarta.persistence.nonJtaDataSource", dataSource,
                "jakarta.persistence.validation.mode", ValidationMode.CALLBACK.name()
            );
            entityManagerFactory = Persistence.createEntityManagerFactory("jpa-demo", properties);
            return entityManagerFactory;
        }

        //        @Produces
        //        TransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        //            return new TransactionManager(entityManagerFactory);
        //        }

        //        @Produces
        //        @Default
        //        EntityManager create(EntityManagerFactory factory) {
        //            return factory.createEntityManager();
        //        }

        @PreDestroy
        public void destroy () {
            // System.out.println("close emf");
            // if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            // entityManagerFactory.close();
            // }
            if ( this.dataSource != null

                 && dataSource instanceof HikariDataSource hikariDataSource ) {
                System.out.println("Close data source");
                hikariDataSource.close();
            }

        }

        // @PreDestroy
        // public void shutdown() {
        //     if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
        //         System.out.println("Closing EntityManagerFactory...");
        //         entityManagerFactory.close();
        //     }
        // }
    }

    @SneakyThrows
    public static <T> T with (Class<T> appClass) {

        var instance = SeContainerInitializer.newInstance();
        CDIConfig ds = null;
        try (var container = instance.addBeanClasses(appClass, CDIConfig.class).initialize()) {
            ds = container.select(CDIConfig.class).get();
            return container.select(appClass).get();
        } finally {
            Thread.sleep(100);
            if (ds != null && ds.dataSource instanceof HikariDataSource dataSource) {
                dataSource.close();
            }
        }

    }
}
