package com.example.campustrack.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name="students", uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 2, max = 80)
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String phone;
    @NotBlank
    private String course;
    @NotBlank
    @Column(name = "academic_year")
    private String year;
    @Min(0)
    @Max(100)
    private int attendance;
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private double marks;
    private LocalDate joinedOn = LocalDate.now();

    public Student() {
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getCourse() { return course; }
    public String getYear() { return year; }
    public int getAttendance() { return attendance; }
    public double getMarks() { return marks; }
    public LocalDate getJoinedOn() { return joinedOn; }
    public void setId(Long value) { id = value; }
    public void setName(String value) { name = value; }
    public void setEmail(String value) { email = value; }
    public void setPhone(String value) { phone = value; }
    public void setCourse(String value) { course = value; }
    public void setYear(String value) { year = value; }
    public void setAttendance(int value) { attendance = value; }
    public void setMarks(double value) { marks = value; }
    public void setJoinedOn(LocalDate value) { joinedOn = value; }
}