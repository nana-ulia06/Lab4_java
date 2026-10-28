package com.university.model;

import java.time.LocalDateTime;

public class Enrollment {
    private final Student student;
    private final Course course;
    private final LocalDateTime enrollmentDate;

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.enrollmentDate = LocalDateTime.now();
    }

    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }

    @Override
    public String toString() {
        return "Enrollment{student=" + student.getFullName()
                + ", course='" + course.getTitle()
                + "', date=" + enrollmentDate + "}";
    }
}