package nashtech.rookies.jpa;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.core.io.ClassPathResource;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JDBCApp {

    record Employee(Long id, String firstName, String lastName, String idCard) {

    }

    public static void main (String[] args) throws SQLException, ClassNotFoundException, IOException {
        var env = Dotenv.load();
        var app = new JDBCApp();
        try (var conn = app.getConnection(env)) {
            var file = new ClassPathResource("jdbc_data.sql").getFile();
            var script = new String(Files.readAllBytes(file.toPath()));
            try (var initStmt = conn.createStatement()) {
                initStmt.execute(script);
            }

            try (var select = conn.createStatement()) {
                var rs = select.executeQuery("SELECT id, first_name, last_name, id_card from employee");
                var employees = new ArrayList<Employee>();
                while ( rs.next() ) {
                    employees.add(
                        new Employee(
                            Long.valueOf(rs.getLong("id")),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("id_card")
                        ))
                    ;
                }
                log.info(employees);
            }
        }
    }

    Connection getConnection (Dotenv dotenv) throws ClassNotFoundException, SQLException {
        Class.forName(dotenv.get("h2.datasource.driverClassName"));
        return DriverManager.getConnection(
            dotenv.get("h2.datasource.url"),
            dotenv.get("h2.datasource.username"),
            dotenv.get("h2.datasource.password")
        );
    }


}
