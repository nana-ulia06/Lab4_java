package com.university.service;

public class ConsoleNotificationService implements NotificationService {
    @Override
    public void notify(String message) {
        System.out.println("notification");
    }
}
