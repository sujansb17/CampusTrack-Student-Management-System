package com.example.campustrack.controller;

import com.example.campustrack.model.Student;
import com.example.campustrack.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class StudentController {
    private final StudentRepository repo;

    public StudentController(StudentRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Student> all(@RequestParam(required = false) String course) {
        return course == null || course.isBlank()
                ? repo.findAll()
                : repo.findByCourseIgnoreCase(course);
    }

    @GetMapping("/{id}")
    public Student one(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student create(@Valid @RequestBody Student student) {
        if (repo.existsByEmailIgnoreCase(student.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        return repo.save(student);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @Valid @RequestBody Student input) {
        Student student = one(id);
        if (repo.existsByEmailIgnoreCaseAndIdNot(input.getEmail(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Email already belongs to another student");
        }
        student.setName(input.getName());
        student.setEmail(input.getEmail());
        student.setPhone(input.getPhone());
        student.setCourse(input.getCourse());
        student.setYear(input.getYear());
        student.setAttendance(input.getAttendance());
        student.setMarks(input.getMarks());
        return repo.save(student);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repo.delete(one(id));
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        List<Student> all = repo.findAll();
        double average = all.stream().mapToDouble(Student::getMarks).average().orElse(0);
        long lowAttendance = all.stream().filter(student -> student.getAttendance() < 75).count();
        return Map.of(
                "total", all.size(),
                "averageMarks", Math.round(average * 10.0) / 10.0,
                "lowAttendance", lowAttendance,
                "courses", all.stream().map(Student::getCourse).distinct().count());
    }
}