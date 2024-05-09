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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import nashtech.rookies.jpa.repository.AuthorRepository;
import nashtech.rookies.jpa.repository.BookRepository;
import nashtech.rookies.jpa.repository.noboot.AuthorRepositoryNoBoot;
import nashtech.rookies.jpa.repository.noboot.BookRepositoryNoBoot;
import nashtech.rookies.jpa.service.AuthorService;
import nashtech.rookies.jpa.service.BookService;
import nashtech.rookies.jpa.service.impl.AuthorServiceImpl;
import nashtech.rookies.jpa.service.impl.BookServiceImpl;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.ValidationMode;
import lombok.SneakyThrows;

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
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
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
    AuthorRepository authorRepository () {
        return new AuthorRepositoryNoBoot();
    }

    @Bean
    BookRepository bookRepository () {
        return new BookRepositoryNoBoot();
    }

    @Bean
    BookService bookService (BookRepository bookRepository) {
        return new BookServiceImpl(bookRepository);
    }

    @Bean
    AuthorService authorService (AuthorRepository authorRepository) {
        return new AuthorServiceImpl(authorRepository);
    }

}
