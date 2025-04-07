package nashtech.rookies.jpa.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

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
        config.setDriverClassName(dotenv.get("datasource.driverClassName"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return new HikariDataSource(config);
    }


    @Bean
    PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

}
