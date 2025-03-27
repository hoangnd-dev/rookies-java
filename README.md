# Nashtech Java Rookie Program Sample

### Requirement
- Java 21
- Spring Boot 3.4.4
- 

### Run

- JDBC
```shell
# H2
mvn -pl jpa package exec:java -Dexec.mainClass="nashtech.rookies.jpa.JDBCApp"
# PG
mvn -pl jpa -P-h2,pg exec:java -Dexec.mainClass="nashtech.rookies.jpa.JDBCApp"
``` 
- JPA
- Spring JPA