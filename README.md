# CampusTrack – Student Management System

A Java Full Stack student administration dashboard built using HTML, CSS, JavaScript, Java 17, Spring Boot, Spring Data JPA and H2/MySQL.

## Features
- Dashboard summary: student count, courses, average marks and low attendance
- Create, view, update and delete student records
- Search by student name, email or course
- Filter by course
- Attendance and marks tracking
- Low-attendance indicator (below 75%)
- Export student records to CSV
- Duplicate email validation
- REST API with server-side validation
- H2 database for quick local startup; optional MySQL profile
- Sample records automatically inserted on first run

## Requirements
- JDK 17+
- Maven 3.6+
- IntelliJ IDEA (recommended)

## Run in IntelliJ
1. Open the `CampusTrack` folder as a Maven project.
3. Wait for Maven dependencies to download.
4. Run `CampusTrackApplication.java`.
5. Visit **http://localhost:8080**.

Or use the terminal:
```bash
mvn spring-boot:run
```

Default database is H2 file storage. H2 console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/campustrack`
- Username: `sa`
- Password: leave blank

## Use MySQL
1. Start MySQL.
2. Open `src/main/resources/application-mysql.properties`.
3. Replace `YOUR_MYSQL_PASSWORD` with your MySQL password.
4. Run:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

## REST API
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/students` | List all students |
| GET | `/api/students?course=BCA` | Filter by course |
| GET | `/api/students/{id}` | Get one student |
| POST | `/api/students` | Add student |
| PUT | `/api/students/{id}` | Update student |
| DELETE | `/api/students/{id}` | Delete student |
| GET | `/api/students/stats` | Dashboard statistics |

Example POST body:
```json
{
  "name": "Sana Khan",
  "email": "sana@example.com",
  "phone": "9876543210",
  "course": "BCA",
  "year": "3rd Year",
  "attendance": 90,
  "marks": 85.5
}
```

## Important
This is an educational portfolio project, not a production college system. It currently has no login or role-based access. Before public deployment, add Spring Security, admin/faculty/student roles, audit logging, stronger privacy controls, backups and production database settings. The sample records are fictional.
