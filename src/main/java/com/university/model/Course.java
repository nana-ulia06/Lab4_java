package com.university.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {
    private final int id;
    private final String title;
    private final int maxStudents;
    private final List<Student> enrolledStudents;

    public Course(int id, String title, int maxStudents) {
        this.id = id;
        this.title = title;
        this.maxStudents = maxStudents;
        this.enrolledStudents = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getMaxStudents() { return maxStudents; }
    public List<Student> getEnrolledStudents() { return new ArrayList<>(enrolledStudents); }

    public boolean isFull() {
        return enrolledStudents.size() >= maxStudents;
    }

    public boolean isStudentEnrolled(Student student) {
        return enrolledStudents.contains(student);
    }

    public void addStudent(Student student) {
        enrolledStudents.add(student);
    }

    public void removeStudent(Student student) {
        enrolledStudents.remove(student);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Course{id=" + id + ", title='" + title + "', enrolled="
                + enrolledStudents.size() + "/" + maxStudents + "}";
    }
}