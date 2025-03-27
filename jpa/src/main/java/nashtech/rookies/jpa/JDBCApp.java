package nashtech.rookies.jpa;

import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Log4j2
@State(Scope.Thread)
public class JDBCApp {

    private final Dotenv       dotenv;
    private final ObjectMapper objectMapper;

    public JDBCApp () {
        dotenv = Dotenv.load();
        objectMapper = new ObjectMapper();
    }

    @Builder
    record EmployeeAsset(
        @JsonProperty("asset_name") String assetName) {
    }

    @Builder
    record Employee(Long id,
                    @JsonProperty("first_name")
                    String firstName,
                    @JsonProperty("last_name")
                    String lastName,
                    @JsonProperty("id_card")
                    String idCard,
                    @JsonProperty("assets")
                    List<EmployeeAsset> assets) {

    }

    @SneakyThrows
    @Benchmark
    public void run () {
        try (var conn = this.getConnection(this.dotenv)) {
            var file = new ClassPathResource("jdbc_data.json").getFile();
            var employees = objectMapper.readValue(
                file, new TypeReference<List<Employee>>() {
                }
            );
            var fileSql = new ClassPathResource("jdbc_data.sql").getFile();
            var script  = new String(Files.readAllBytes(fileSql.toPath()));
            try (var initStmt = conn.createStatement()) {
                initStmt.execute(script);
            }
            employees.forEach(employee -> {
                long saveId       = this.saveUser(employee, conn);
                var  userAssetIds = this.saveUserAssert(saveId, employee.assets(), conn);
                System.out.println(userAssetIds);
                System.out.printf("save %s with id %d\n and assetIds %s", employee, saveId, userAssetIds);
            });
            var employees1 = new ArrayList<Employee>();
            try (var select = conn.createStatement()) {
                var rs = select.executeQuery("SELECT id, first_name, last_name, id_card from employee");
                while ( rs.next() ) {
                    employees1.add(
                        new Employee(
                            rs.getLong("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("id_card"),
                            // why null, lazy load or n+1
                            List.of()
                        ))
                    ;
                }
            }
            employees1.forEach(log::info);
        }
    }

    public static void main (String[] args) {
        new JDBCApp().run();
    }


    List<Long> saveUserAssert (long employeeId, List<EmployeeAsset> employeeAsset, Connection connection) {
        if ( Objects.isNull(employeeAsset) ) {
            return List.of();
        }
        final String sql = "INSERT INTO employee_asset (employee_id, asset_name) VALUES (?, ?)";
        return employeeAsset.stream().map(e -> {
            try (var stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, employeeId);
                stmt.setString(2, e.assetName());
                int affectedRows = stmt.executeUpdate();
                if ( affectedRows > 0 ) {
                    try (var rs = stmt.getGeneratedKeys()) {
                        if ( rs.next() ) {
                            return rs.getLong(1);
                        }
                    }
                }
            }
            catch (SQLException ignored) {
                log.error(ignored);
            }
            return Long.valueOf(-1);
        }).toList();
    }

    @SneakyThrows
    long saveUser (Employee employee, Connection connection) {
        String sql = "INSERT INTO employee (first_name, last_name, id_card) VALUES (?, ?, ?)";
        try (var stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, employee.firstName());
            stmt.setString(2, employee.lastName());
            stmt.setString(3, employee.idCard());
            int affectedRows = stmt.executeUpdate();
            if ( affectedRows > 0 ) {
                try (var rs = stmt.getGeneratedKeys()) {
                    if ( rs.next() ) {
                        return rs.getLong(1);
                    }
                }
            }
        }
        return -1;
    }

    Connection getConnection (Dotenv dotenv) throws SQLException {
        return DriverManager.getConnection(
            dotenv.get("datasource.url"),
            dotenv.get("datasource.username"),
            dotenv.get("datasource.password")
        );
    }


}
