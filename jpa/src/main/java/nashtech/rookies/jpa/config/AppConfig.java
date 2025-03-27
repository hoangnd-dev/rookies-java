package nashtech.rookies.jpa.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.ValidationMode;
import lombok.SneakyThrows;
import nashtech.rookies.jpa.repository.RoleRepository;
import nashtech.rookies.jpa.repository.UserRepository;
import nashtech.rookies.jpa.repository.noboot.RoleRepositoryNoBoot;
import nashtech.rookies.jpa.repository.noboot.UserRepositoryNoBoot;
import nashtech.rookies.jpa.service.RoleService;
import nashtech.rookies.jpa.service.UserService;
import nashtech.rookies.jpa.service.impl.RoleServiceImpl;
import nashtech.rookies.jpa.service.impl.UserServiceImpl;

@Configuration
@Profile("jpa")
public class AppConfig {

    private static final List<AutoCloseable> autoCloseables = new ArrayList<>();


    AppConfig () {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> autoCloseables.forEach(this::closeObject)));
    }


    @Bean
    Dotenv dotenv () {
        return Dotenv.load();
    }

    @Bean
    DataSource dataSource (Dotenv dotenv) {
        var config = new HikariConfig();
        config.setJdbcUrl(dotenv.get("h2.datasource.url"));
        config.setUsername(dotenv.get("h2.datasource.username"));
        config.setPassword(dotenv.get("h2.datasource.password"));
        config.setDriverClassName(dotenv.get("h2.datasource.driverClassName"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        HikariDataSource hikariDataSource = new HikariDataSource(config);
        autoCloseables.add(hikariDataSource);
        return hikariDataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate (DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


    @Bean
    EntityManagerFactory entityManagerFactory (DataSource dataSource) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.nonJtaDataSource", dataSource);
        properties.put("jakarta.persistence.validation.mode", ValidationMode.CALLBACK.name());
        return Persistence.createEntityManagerFactory("jpa-demo", properties);
    }


    @Bean
    JpaTransactionManager transactionManager (EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


    @SneakyThrows
    private void closeObject (AutoCloseable closeable) {
        closeable.close();
    }

    @Bean
    RoleRepository roleRepository () {
        return new RoleRepositoryNoBoot();
    }

    @Bean
    UserRepository userRepository () {
        return new UserRepositoryNoBoot();
    }

    @Bean
    RoleService roleService () {
        return new RoleServiceImpl(roleRepository());
    }

    @Bean
    PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserService userService () {
        return new UserServiceImpl(userRepository(), roleRepository(), passwordEncoder());
    }

}
