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

The `blood_donor_management` database already exists. The application connects to the MySQL server on the same computer at `localhost:3306`. It does not create, delete, or recreate tables; `spring.jpa.hibernate.ddl-auto=none` is set.

On your Mac, make sure the Homebrew MySQL service is running. The usual service command is:

```bash
brew services start mysql
```

If you installed a versioned formula, check `brew services list` and start that MySQL service instead. Set the credentials in the same Mac terminal session you will use to start the app:

```zsh
export DB_USERNAME="your_mysql_username"
read -s "DB_PASSWORD?MySQL password: "
export DB_PASSWORD
```

The password is read without echoing it or putting it directly in the command history. `DB_URL` is optional; its default is `jdbc:mysql://localhost:3306/blood_donor_management`.

The optional `application-local.properties` file can override local connection settings. Copy `src/main/resources/application-local.properties.example` to `application-local.properties` in this project folder if needed. The real file is ignored by Git, and its username and password entries still read from `DB_USERNAME` and `DB_PASSWORD`.

Replit runs in the cloud and cannot reach MySQL on your Mac's `localhost`. Run this application on your Mac to use that database.

## Run locally

From this folder, start the app with:

```bash
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080).

To check the database connection, open [http://localhost:8080/db-check](http://localhost:8080/db-check). It returns only `{"status":"connected"}` or a generic unavailable response; it does not include credentials or connection details.

To create a packaged JAR:

```bash
mvn clean package
```

The sign-in screen is a visual prototype only, so Spring Security is not included and authentication is not active.
