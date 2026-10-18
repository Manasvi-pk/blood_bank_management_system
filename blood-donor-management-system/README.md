# Blood Donor Management System

A Java 21 and Spring Boot college OOPS project with a Thymeleaf blood bank interface. The current stage contains a dashboard and prototype screens for donor management, recipients, donation records, blood inventory, requests, and sign-in. The displayed records are sample data only; forms do not save data, and no database entities or tables are included yet.

## Requirements

- JDK 21
- Maven 3.9 or later
- MySQL Server

Check Java and Maven from a terminal:

```bash
java -version
mvn -version
```

## Configure MySQL

Create an empty database in MySQL:

```sql
CREATE DATABASE blood_donor_management;
```

Copy `src/main/resources/application-local.properties.example` to `application-local.properties` in this project folder. Update the username and password there with your local MySQL credentials. The real `application-local.properties` file is ignored by Git, so credentials are not committed.

Alternatively, set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` as environment variables before starting the application.

## Run locally

From this folder, start the app with:

```bash
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080).

To create a packaged JAR:

```bash
mvn clean package
```

The app uses `spring.jpa.hibernate.ddl-auto=none`; it will not create or modify database tables. The sign-in screen is a visual prototype only, so Spring Security is not included and authentication is not active.
