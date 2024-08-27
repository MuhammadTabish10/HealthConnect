package com.healthconnect.userservice.service.impl;

import com.healthconnect.userservice.service.EmailService;
import com.healthconnect.userservice.util.EmailTemplateProvider;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.healthconnect.userservice.constant.LogMessages.*;
import static com.healthconnect.userservice.constant.UserConstants.ENCODING;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    @Override
    public void sendPasswordResetEmail(String email, String firstName, String token) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, ENCODING);

            helper.setFrom(sender);
            helper.setTo(email);
            helper.setSubject(EmailTemplateProvider.PASSWORD_RESET_SUBJECT);
            helper.setText(EmailTemplateProvider.getPasswordResetEmailContent(firstName, token), true);

            javaMailSender.send(mimeMessage);
            logger.info(String.format(PASSWORD_RESET_EMAIL_SUCCESS, email, firstName));
        } catch (MessagingException e) {
            logger.error(String.format(PASSWORD_RESET_EMAIL_FAILED, email), e);
        }
    }


}
