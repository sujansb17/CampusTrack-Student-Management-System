package com.example.campustrack.config;

import com.example.campustrack.model.Student;
import com.example.campustrack.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seed(StudentRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                add(repo, "Aarav Sharma", "aarav@example.com", "9876543210", "BCA", "3rd Year", 92, 86);
                add(repo, "Meera Nair", "meera@example.com", "9876543211", "BCA", "3rd Year", 68, 91);
                add(repo, "Rohan Shetty", "rohan@example.com", "9876543212", "B.Com", "2nd Year", 81, 74);
                add(repo, "Ananya Rao", "ananya@example.com", "9876543213", "BBA", "1st Year", 96, 88);
                add(repo, "Kabir Khan", "kabir@example.com", "9876543214", "BCA", "2nd Year", 72, 79);
                add(repo, "Diya Fernandes", "diya@example.com", "9876543215", "B.Sc", "3rd Year", 88, 94);
            }
        };
    }

    private void add(StudentRepository repository, String name, String email, String phone,
            String course, String year, int attendance, double marks) {
        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setPhone(phone);
        student.setCourse(course);
        student.setYear(year);
        student.setAttendance(attendance);
        student.setMarks(marks);
        repository.save(student);
    }
}