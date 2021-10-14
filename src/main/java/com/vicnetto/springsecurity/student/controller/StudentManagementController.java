package com.vicnetto.springsecurity.student.controller;

import com.vicnetto.springsecurity.student.model.Student;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("management/api/v1/students")
public class StudentManagementController {

    private static final List<Student> STUDENTS = List.of(
            new Student(1, "Victor Netto"),
            new Student(2, "Beatriz Lopes"),
            new Student(3, "Eden Netto"),
            new Student(4, "Allana Netto")
    );

    @GetMapping
    public List<Student> getStudents() {
        return STUDENTS;
    }

    @PostMapping
    public void registerStudent(@RequestBody Student student) {
        System.out.println("Registering new student: " + student.getId() + " | " + student.getName());
    }

    @DeleteMapping(path = "/{id}")
    public void deleteStudent(@PathVariable("id") Integer studentId) {
        System.out.println("Deleting student: " + studentId);
    }

    @PutMapping(path = "/{id}")
    public void updateStudent(@PathVariable("id") Integer studentId, @RequestBody Student student) {
        System.out.println("Updating student: " + studentId + " | " + student.getName());
    }
}