package com.university.model;

import java.util.Objects;

public class Student {
    private final int id;
    private final String firstName;
    private final String lastName;

    public Student(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getID() {return id;}
    public String getFN() { return firstName;};
    public String getLN() { return lastName;};

    public String getFullName() {
        return lastName + " " + firstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;
        return id == student.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Student: id=" + id + ". Name: " + getFullName();
    }
}