package com.backend.movie_matrix.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtpMail(String toEmail, String otp) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("vamshidasari07@gmail.com");
            message.setTo(toEmail);
            message.setSubject("OTP For Reset Password");
            message.setText(buildMessage(otp));

            mailSender.send(message);
            log.info("✅ Email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.info("✅ Email sent successfully to: {}", toEmail);
            throw new RuntimeException("Failed to send mail: " + e.getMessage(), e);
        }
    }

    public String buildMessage(String otp) {
        return String.format("""
                Hello from Movie Matrix! 🎬

                Your OTP for password reset is: %s

                This OTP will expire in 5 minutes.

                If you did not request this, please ignore this email.

                Best regards,
                Movie Matrix Team
                """, otp);
    }

}
