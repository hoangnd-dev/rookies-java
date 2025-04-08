# Nashtech Java Rookie Program Sample

### Requirement
- Java 21
- Spring Boot 3.4.4

### Setup

Copy `.env.sample` to `.env`
Change the value to your environment

### JDBC
```shell
mvn -pl jpa exec:java -Dexec.mainClass="nashtech.rookies.jpa.JDBCApp"
```

### JPA 
```shell
mvn -pl jpa exec:java -Dexec.mainClass="nashtech.rookies.jpa.JPAApp"
``` 
### Spring JPA
```shell
mvn -pl jpa package exec:java -Dexec.mainClass=nashtech.rookies.jpa.SpringDataApplication 
```

### Spring JWT
```shell
mvn -pl security-jwt exec:java -Dexec.mainClass=nashtech.rookies.securiry.JwtApplication
```
http://localhost:8080/api/swagger-ui/index.html