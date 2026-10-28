package com.university;

import com.university.model.Course;
import com.university.model.Student;
import com.university.repository.CourseRepository;
import com.university.repository.InMemoryCourseRepository;
import com.university.service.ConsoleNotificationService;
import com.university.service.EnrollmentService;
import com.university.service.NotificationService;

public class Main {
    public static void main(String[] args) {
        // Dependency Injection: создаём зависимости и внедряем через конструктор
        CourseRepository repository = new InMemoryCourseRepository();
        NotificationService notificationService = new ConsoleNotificationService();
        EnrollmentService enrollmentService = new EnrollmentService(repository, notificationService);

        Course zinfCourse = new Course(1, "Защита информации", 3);
        Course dbCourse = new Course(2, "Базы данных", 2);
        repository.save(zinfCourse);
        repository.save(dbCourse);

        Student stud1 = new Student(101, "Никола", "Тесла");
        Student stud2 = new Student(102, "Илон", "Маск");
        Student stud3 = new Student(103, "Себастьян", "Паскаль");

        enrollmentService.enrollStudent(stud1, 1);
        enrollmentService.enrollStudent(stud2, 1);

        // Попытка повторной записи
        try {
            enrollmentService.enrollStudent(stud1, 1);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        enrollmentService.enrollStudent(stud3, 1);

        Student stud4 = new Student(104, "Исаак", "Ньютон");
        try {
            enrollmentService.enrollStudent(stud4, 1);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\nСостояние курсов");
        repository.findAll().forEach(System.out::println);
    }
}