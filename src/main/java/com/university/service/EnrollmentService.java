package com.university.service;

import com.university.model.Course;
import com.university.model.Enrollment;
import com.university.model.Student;
import com.university.repository.CourseRepository;

public class EnrollmentService {
    private final CourseRepository courseRepository;
    private final NotificationService notificationService;

    public EnrollmentService(CourseRepository courseRepository,
                             NotificationService notificationService) {
        this.courseRepository = courseRepository;
        this.notificationService = notificationService;
    }

    /**
     * Записывает студента на курс.
     * @throws IllegalArgumentException если курс не найден
     * @throws IllegalStateException если студент уже записан или курс заполнен
     */
    public Enrollment enrollStudent(Student student, int courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Курс с id=" + courseId + " не найден"));

        // Индивидуальное задание: запрет повторной записи
        if (course.isStudentEnrolled(student)) {
            String msg = "Студент " + student.getFullName()
                    + " уже записан на курс '" + course.getTitle() + "'";
            notificationService.notify(msg);
            throw new IllegalStateException(msg);
        }

        if (course.isFull()) {
            String msg = "Курс '" + course.getTitle() + "' заполнен";
            notificationService.notify(msg);
            throw new IllegalStateException(msg);
        }

        course.addStudent(student);
        courseRepository.save(course);

        Enrollment enrollment = new Enrollment(student, course);
        notificationService.notify("Студент " + student.getFullName()
                + " записан на курс '" + course.getTitle() + "'");
        return enrollment;
    }

    public void cancelEnrollment(Student student, int courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Курс с id=" + courseId + " не найден"));

        if (!course.isStudentEnrolled(student)) {
            throw new IllegalStateException("Студент не записан на курс");
        }

        course.removeStudent(student);
        courseRepository.save(course);
        notificationService.notify("Запись студента " + student.getFullName()
                + " на курс '" + course.getTitle() + "' отменена");
    }
}