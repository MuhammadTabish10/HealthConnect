package com.healthconnect.userservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplateProvider {

    public static final String PASSWORD_RESET_SUBJECT = "HealthConnect: Password Reset Request";

    private static final String PASSWORD_RESET_TEMPLATE =
            "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;\">" +
                    "<h2 style=\"color: #2c3e50; text-align: center;\">Password Reset Request</h2>" +
                    "<p style=\"font-size: 16px; color: #34495e;\">Dear %s,</p>" +
                    "<p style=\"font-size: 16px; color: #34495e;\">" +
                    "We received a request to reset your password for your HealthConnect account. " +
                    "Please use the following token to reset your password:</p>" +
                    "<p style=\"font-size: 18px; color: #e74c3c; text-align: center; font-weight: bold;\">" +
                    "%s" +
                    "</p>" +
                    "<p style=\"font-size: 16px; color: #34495e;\">" +
                    "This token will expire in 5 minutes. If you did not request a password reset, please " +
                    "contact our support team immediately at " +
                    "<a href=\"mailto:support@healthconnect.com\" style=\"color: #3498db; text-decoration: none;\">support@healthconnect.com</a>.</p>" +
                    "<p style=\"font-size: 16px; color: #34495e;\">Best regards,<br>The HealthConnect Team</p>" +
                    "</div>";

    public static String getPasswordResetEmailContent(String userName, String token) {
        return String.format(PASSWORD_RESET_TEMPLATE, userName, token);
    }

}
