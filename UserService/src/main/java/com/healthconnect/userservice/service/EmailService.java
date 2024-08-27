package com.healthconnect.userservice.service;

public interface EmailService {
    void sendPasswordResetEmail(String email, String firstName, String token);
}
