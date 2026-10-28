package com.university.repository;
import com.university.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    void save(Course course);
    Optional<Course> findById(int id);
    List<Course> findAll();
    void deleteById(int id);
}
