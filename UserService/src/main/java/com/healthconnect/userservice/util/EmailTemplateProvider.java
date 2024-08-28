package com.healthconnect.userservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplateProvider {

    public static final String PASSWORD_RESET_SUBJECT = "HealthConnect: Password Reset Request";

    private static final String LOGO_URL = "https://firebasestorage.googleapis.com/v0/b/healthconnect-ccab4.appspot.com/o/logo.png?alt=media";

    private static final String PASSWORD_RESET_TEMPLATE =
            "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9;\">" +
                    "<div style=\"text-align: center; margin-bottom: 20px;\">" +
                    "<img src=\"" + LOGO_URL + "\" alt=\"HealthConnect\" style=\"max-width: 150px; pointer-events: none; cursor: default;\"/>" +
                    "</div>" +
                    "<h2 style=\"color: #2c3e50; text-align: center; font-size: 24px;\">Password Reset Request</h2>" +
                    "<p style=\"font-size: 16px; color: #34495e;\">Hello %s,</p>" +
                    "<p style=\"font-size: 16px; color: #34495e;\">" +
                    "We received a request to reset your password for your HealthConnect account. " +
                    "Please use the following token to reset your password:</p>" +
                    "<div style=\"text-align: center; margin: 20px 0;\">" +
                    "<span style=\"font-size: 20px; color: #e74c3c; font-weight: bold; padding: 10px 20px; background-color: #fff; border: 1px solid #e74c3c; border-radius: 5px;\">" +
                    "%s" +
                    "</span>" +
                    "</div>" +
                    "<p style=\"font-size: 16px; color: #34495e;\">" +
                    "This token will expire in 5 minutes. If you did not request a password reset, please " +
                    "contact our support team immediately at " +
                    "<a href=\"mailto:support@healthconnect.com\" style=\"color: #3498db; text-decoration: none;\">support@healthconnect.com</a>.</p>" +
                    "<p style=\"font-size: 16px; color: #34495e;\">Best regards,<br>The Health Connect Team</p>" +
                    "</div>";


    public static String getPasswordResetEmailContent(String userName, String token) {
        return String.format(PASSWORD_RESET_TEMPLATE, userName, token);
    }
}
