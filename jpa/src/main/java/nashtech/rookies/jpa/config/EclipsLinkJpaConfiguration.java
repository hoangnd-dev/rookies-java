package nashtech.rookies.jpa.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.logging.SessionLog;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;

// EclipseLink Specific Configuration Class
//@Configuration
public class EclipsLinkJpaConfiguration extends JpaBaseConfiguration {

    protected EclipsLinkJpaConfiguration(DataSource dataSource, JpaProperties properties,
                                         ObjectProvider<JtaTransactionManager> jtaTransactionManager) {
        super(dataSource, properties, jtaTransactionManager);
    }

    // EclipseLink JPA Adaptor
    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new EclipseLinkJpaVendorAdapter();
    }

    @Override
    protected Map<String, Object> getVendorProperties (DataSource dataSource) {
        Map<String, Object> map = new HashMap<>();
        map.put(PersistenceUnitProperties.WEAVING, "false");
        map.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, dataSource);
        map.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.CREATE_ONLY);
        map.put(PersistenceUnitProperties.LOGGING_LEVEL, SessionLog.FINER_LABEL);
        return map;
    }



//    // Database Connection properties setup
//    @Bean
//    public static DataSource dataSource() {
//        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://localhost:3306/tutorialspoint");
//        dataSource.setUsername("root");
//        dataSource.setPassword("root@123");
//        return dataSource;
//    }
}