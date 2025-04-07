# Nashtech Java Rookie Program Sample

### Requirement
- Java 21
- Spring Boot 3.4.4



### Setup

Copy `.env.sample` to `.env`
Change the value to your environment

### JDBC
H2 database
```shell
mvn -pl jpa package exec:java -Dexec.mainClass="nashtech.rookies.jpa.JDBCApp"
```

Postgres database
```shell
mvn -pl jpa -P-h2,pg exec:java -Dexec.mainClass="nashtech.rookies.jpa.JDBCApp"
``` 
- Spring JPA
```shell
mvn -pl jpa package exec:java -Dexec.mainClass=nashtech.rookies.jpa.SpringDataApplication 
```