package com.university.service;

import com.university.model.Course;
import com.university.model.Enrollment;
import com.university.model.Student;
import com.university.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date; // Нарушение UnusedImports

public class EnrollmentService {
    private static final Logger log = LoggerFactory.getLogger(EnrollmentService.class);

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
        log.info("Запрос на запись студента '{}' на курс с id={}",
                student.getFullName(), courseId);

        Course course;
        try {
            course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Курс с id=" + courseId + " не найден"));
        } catch (IllegalArgumentException e) {
            log.error("writeln error: {}", e.getMessage());
            throw e;
        }

        // Индивидуальное задание: запрет повторной записи
        if (course.isStudentEnrolled(student)) {
            String msg = "Студент " + student.getFullName()
                    + " уже записан на курс '" + course.getTitle() + "'";

            log.warn("попытка повторной записи: {}", msg);

            notificationService.notify(msg);
            throw new IllegalStateException(msg);
        }

        if (course.isFull()) {
            String msg = "Курс '" + course.getTitle() + "' заполнен";

            log.warn("попытка записи на заполненный курс: Студент '{}'," +
                    " Курс '{}'", student.getFullName(), course.getTitle());

            notificationService.notify(msg);
            throw new IllegalStateException(msg);
        }

        course.addStudent(student);
        courseRepository.save(course);

        Enrollment enrollment = new Enrollment(student, course);

        log.info("Студент '{}' успешно записан на курс '{}",
                student.getFullName(), course.getTitle());

        notificationService.notify("Студент " + student.getFullName()
                + " записан на курс '" + course.getTitle() + "'");
        return enrollment;
    }

    public void cancelEnrollment(Student student, int courseId) {

        log.info("Запрос на отмену записи студента '{}' с курса с id={}",
                student.getFullName(), courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.error("Ошибка при отмене: Курс с id={} не найден", courseId);

                    return new IllegalArgumentException("Курс с id=" + courseId + " не найден");
                });

        if (!course.isStudentEnrolled(student)) {

            log.warn("Студент '{}' не записан на курс '{}'. Отмена невозможна.",
                    student.getFullName(), course.getTitle());

            throw new IllegalStateException("Студент не записан на курс");
        }

        course.removeStudent(student);
        courseRepository.save(course);

        log.info("Запись студента '{}' на курс '{}' успешно отменена",
                student.getFullName(), course.getTitle());

        notificationService.notify("Запись студента " + student.getFullName()
                + " на курс '" + course.getTitle() + "' отменена");
    }
}