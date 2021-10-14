package com.vicnetto.springsecurity.student.controller;

import com.vicnetto.springsecurity.student.model.Student;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private static final List<Student> STUDENTS = List.of(
            new Student(1, "Victor Netto"),
            new Student(2, "Beatriz Lopes"),
            new Student(3, "Eden Netto"),
            new Student(4, "Allana Netto")
    );

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public Student getStudent(@PathVariable Integer id) {
        return STUDENTS.stream()
                .filter(student -> id.equals(student.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("There is no student with this ID."));
    }
}