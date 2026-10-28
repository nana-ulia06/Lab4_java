package com.university.repository;

import com.university.model.Course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryCourseRepository implements CourseRepository {
    private final Map<Integer, Course> storage = new HashMap<>();

    @Override
    public void save(Course course) {
        storage.put(course.getId(), course);
    }

    @Override
    public Optional<Course> findById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Course> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(int id) {
        storage.remove(id);
    }
}