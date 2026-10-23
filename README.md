# EduNova-Student-Registration-and-Management-system
EduNova is a web-based Student Management System built using Java, Spring Boot, MySQL, HTML, CSS, and JavaScript. It enables student registration, stores student details securely in a database, and supports retrieving student records and analyzing course-wise registrations.
# EduNova – Student Management System

EduNova is a web-based Student Management System built with Java, Spring Boot, MySQL, HTML, CSS, and JavaScript. It allows students to submit registration details and stores those records in a MySQL database. The application can retrieve and display registered student information through its web interface.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Project Location](#project-location)
- [Database Setup](#database-setup)
- [Application Configuration](#application-configuration)
- [Run the Application](#run-the-application)
- [Open the Website](#open-the-website)
- [How Data Is Stored and Retrieved](#how-data-is-stored-and-retrieved)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)
- [Security Notes](#security-notes)
- [Future Enhancements](#future-enhancements)

## Features

- Student registration through a web form.
- Stores student registration records in MySQL.
- Displays registered student records through the web application.
- Supports course selection during registration.
- Can calculate course-wise registration totals from stored records.

> Note: Separate secure administrator login, role-based access, and active-session tracking are planned enhancements unless they have been implemented in your current version.

## Technology Stack

- **Backend:** Java and Spring Boot
- **Database:** MySQL
- **Data access:** Spring Data JPA (if configured in the project)
- **Build tool:** Apache Maven
- **Frontend:** HTML, CSS, JavaScript
- **IDE:** Visual Studio Code

## Prerequisites

Install these tools before running the project:

1. **Java JDK 21** (or the Java version configured in the project).
2. **Apache Maven** (the project was previously run with Maven 3.9.11).
3. **MySQL Server**.
4. **Visual Studio Code** or another Java-compatible IDE.
5. **Git**, if you want to clone or upload the project using Git.

Check that Java and Git are available by running these commands in a terminal:

```powershell
java -version
mvn -version
git --version
```

If `mvn` is not recognized, use the Maven executable installed on your computer or configure Maven in your PATH.

## Project Location

The local project folder used during development is:

```text
D:\miniproject\studentmanagement
```

Open this folder in Visual Studio Code using **File → Open Folder**.

## Database Setup

### Step 1: Start MySQL Server

Start the MySQL Server service on your computer. MySQL Workbench does not need to remain open as long as the server service is running.

### Step 2: Create the database

Open MySQL Workbench or a MySQL command-line client and run:

```sql
CREATE DATABASE IF NOT EXISTS student_db;
```

The application uses the `student_db` database. If your current application configuration uses a different database name, use that configured name instead.

### Step 3: Check the table

The application uses a `students` table. If Spring Data JPA is configured to create/update tables automatically, the table may be created when the application starts. Otherwise, create or migrate the table according to the entity definition in the project.

To check whether the table exists and view saved records, run:

```sql
USE student_db;
SHOW TABLES;
SELECT * FROM students;
```

Run the `SELECT` statement only after the `students` table exists.

## Application Configuration

Open:

```text
src/main/resources/application.properties
```

Make sure the datasource configuration matches your local MySQL setup. A typical configuration looks like this:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

This is an example configuration, not a replacement for checking your existing file. Confirm that the MySQL JDBC driver dependency is present in `pom.xml`.

Set the environment variables `DB_USERNAME` and `DB_PASSWORD` to your own MySQL credentials before starting the application, or use a local-only configuration file that is excluded from Git.

**Do not commit real database passwords or other secrets to GitHub.**

## Run the Application

### Option A: Run from the VS Code terminal

1. Open the project folder in VS Code.
2. Start the MySQL Server service.
3. Open **Terminal → New Terminal**.
4. Change to the project directory:

   ```powershell
   cd D:\miniproject\studentmanagement
   ```

5. Run the application with Maven:

   ```powershell
   mvn spring-boot:run
   ```

   If Maven is not on your PATH and you use the previously configured Maven installation, run:

   ```powershell
   cmd.exe /d /c ""%USERPROFILE%\tools\apache-maven-3.9.11\bin\mvn.cmd" spring-boot:run"
   ```

6. Wait for the terminal to report that the Spring Boot application has started successfully.
7. Keep the terminal open while using the local website.

### Option B: Build a JAR

From the project root, run:

```powershell
mvn clean package
```

If the build succeeds, Maven normally places the packaged JAR in the `target` directory. Run the JAR using the actual filename created by your build:

```powershell
java -jar target\your-application-name.jar
```

Replace `your-application-name.jar` with the real JAR filename.

## Open the Website

When the application is running, open these URLs in your browser:

- **Homepage:** http://localhost:8080/
- **Student registration:** http://localhost:8080/students/register
- **Registered students:** http://localhost:8080/students

These URLs work locally while the Spring Boot application is running on your computer. The registered-students page should only be exposed to authorized users before the application is used with real student information.

## How Data Is Stored and Retrieved

1. A student fills out the registration form in the browser.
2. The form submits the details to the Spring Boot application.
3. The Java controller validates/processes the submitted values and uses the repository to save the record.
4. MySQL stores the record in the `students` table.
5. The application can retrieve records from the database and render them on the website.

For example, the following SQL query counts registrations by course:

```sql
SELECT course, COUNT(*) AS total_students
FROM students
GROUP BY course;
```

## Project Structure

The project is expected to follow a structure similar to this. Exact files can vary depending on the current implementation.

```text
studentmanagement/
├── pom.xml
├── README.md
├── .gitignore
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── example/
        │           └── studentmanagement/
        │               ├── StudentmanagementApplication.java
        │               ├── Student.java
        │               ├── StudentRepository.java
        │               ├── StudentController.java
        │               └── HomeController.java
        └── resources/
            ├── application.properties
            └── static/
                └── index.html
```

## Troubleshooting

### MySQL connection error

- Confirm the MySQL Server service is running.
- Check the database name, port, username, and password in your local configuration.
- Confirm the database `student_db` exists.
- Make sure the MySQL JDBC driver dependency is included in `pom.xml`.

### Port 8080 is already in use

Stop the other application using port 8080, or configure a different server port in your local application settings.

### Maven or Java command is not recognized

- Check that Java and Maven are installed.
- Run `java -version` and `mvn -version`.
- If Maven is installed in a custom folder, run its full executable path.

### Student table does not exist

Check your JPA entity and schema-generation configuration. If automatic table creation is disabled, create the table or apply the project's database migration before using the registration form.

### Website cannot be opened

Make sure the Spring Boot application has started without errors and that the terminal running it remains open. Then try `http://localhost:8080/`.

## Security Notes

- Do not upload database passwords, private keys, access tokens, or other secrets to GitHub.
- Do not commit real student personal information or database exports.
- A private GitHub repository does not replace proper secret management.
- Protect student records and administrator pages with server-side authentication and authorization before using the application with real users.
- Do not describe administrator login or active-user tracking as available until those features are implemented and tested.

## Future Enhancements

- Separate administrator login with server-side access control.
- Admin dashboard for registration totals and course-wise statistics.
- Search, update, and delete student records.
- Login activity and active-session tracking.
- Deployment to a hosting platform with a remotely hosted database.

---

## Author

**EduNova – Student Management System**

A learning project demonstrating web development, Java Spring Boot, and MySQL database integration.

