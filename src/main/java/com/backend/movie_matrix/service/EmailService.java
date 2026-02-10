package com.backend.movie_matrix.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    private static final String FROM_EMAIL = "vamshidasari07@gmail.com";

    public void sendOtpMail(String toEmail, String otp) {
        Email from = new Email(FROM_EMAIL);
        Email to = new Email(toEmail);
        String subject = "OTP For Reset Password";

        Content content = new Content(
                "text/plain",
                buildMessage(otp));

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            log.info("📧 SendGrid status: {}", response.getStatusCode());

            if (response.getStatusCode() >= 400) {
                throw new RuntimeException("SendGrid error: " + response.getBody());
            }
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
