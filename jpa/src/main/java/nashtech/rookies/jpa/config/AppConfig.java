package nashtech.rookies.jpa.config;

import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManagerFactory;

@Configuration
public class AppConfig {

    @Bean
    Dotenv dotenv () {
        return Dotenv.load();
    }

    @Bean
    DataSource dataSource (Dotenv dotenv) {
        var config = new HikariConfig();
        config.setJdbcUrl(dotenv.get("datasource.url"));
        config.setUsername(dotenv.get("datasource.username"));
        config.setPassword(dotenv.get("datasource.password"));
        config.addDataSourceProperty("maximumPoolSize", 2);
        config.addDataSourceProperty("cachePrepStmts", "false");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return new HikariDataSource(config);
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory (
        DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaVendorAdapter(new EclipseLinkJpaVendorAdapter());
        factoryBean.setManagedTypes(new PersistenceManagedTypes() {
            @Override
            public List<String> getManagedClassNames () {
                return List.of(
                    "nashtech.rookies.jpa.entity.UserProfileId",
                    "nashtech.rookies.jpa.entity.DepartmentEntity",
                    "nashtech.rookies.jpa.entity.UserEntity",
                    "nashtech.rookies.jpa.entity.UserProfileEntity",
                    "nashtech.rookies.jpa.entity.RoleEntity",
                    "nashtech.rookies.jpa.entity.SetConverter",
                    "nashtech.rookies.jpa.entity.BooleanToYNConverter"
                );
            }

            @Override
            public List<String> getManagedPackages () {
                return List.of();
            }

            @Override
            public URL getPersistenceUnitRootUrl () {
                return null;
            }
        });
        var jpaProperties = Map.of(
            "jakarta.persistence.schema-generation.database.action", "create",
            "eclipselink.schema-generation.output-mode", "database",
            "eclipselink.logging.level", "FINE",
            "eclipselink.logging.parameters", "true"
        );
        factoryBean.setJpaPropertyMap(jpaProperties);
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager (EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


    @Bean
    PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

}
