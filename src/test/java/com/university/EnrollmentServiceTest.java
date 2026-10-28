package com.university;

import com.university.model.Course;
import com.university.model.Enrollment;
import com.university.model.Student;
import com.university.repository.InMemoryCourseRepository;
import com.university.service.EnrollmentService;
import com.university.service.NotificationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnrollmentServiceTest {

    private InMemoryCourseRepository repository;
    private EnrollmentService service;
    private Student student;
    private Course course;

    // Тестовая реализация NotificationService (mock)
    static class TestNotificationService implements NotificationService {
        String lastMessage;

        @Override
        public void notify(String message) {
            lastMessage = message;
        }
    }

    @BeforeEach
    void setUp() {
        repository = new InMemoryCourseRepository();
        TestNotificationService notificationService = new TestNotificationService();
        service = new EnrollmentService(repository, notificationService);

        student = new Student(1, "Julian", "Beckham");
        course = new Course(10, "Java", 3);
        repository.save(course);
    }

    // Тест 1: успешный сценарий
    @Test
    void shouldEnrollStudentSuccessfully() {
        Enrollment enrollment = service.enrollStudent(student, 10);

        assertNotNull(enrollment);
        assertEquals(student, enrollment.getStudent());
        assertEquals(course, enrollment.getCourse());

        Course saved = repository.findById(10).orElseThrow();
        assertTrue(saved.isStudentEnrolled(student));
        assertEquals(1, saved.getEnrolledStudents().size());
    }

    // Тест 2: ошибочный сценарий — курс не найден
    @Test
    void shouldThrowWhenCourseNotFound() {
        assertThrows(IllegalArgumentException.class,
                () -> service.enrollStudent(student, 999));
    }

    // Тест 3: индивидуальное задание — запрет повторной записи
    @Test
    void shouldThrowWhenStudentEnrollsTwice() {
        service.enrollStudent(student, 10);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.enrollStudent(student, 10)
        );

        assertTrue(exception.getMessage().contains("уже записан"));

        Course saved = repository.findById(10).orElseThrow();
        assertEquals(1, saved.getEnrolledStudents().size());
    }

    @Test
    void shouldReturnFullNameInLastFirstNameOrder() {
        assertEquals("Beckham Julian", student.getFullName());
    }
}