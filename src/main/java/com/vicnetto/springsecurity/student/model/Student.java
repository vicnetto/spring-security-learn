package com.vicnetto.springsecurity.student.model;

public class Student {

    private final Integer id;

    private final String name;

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}