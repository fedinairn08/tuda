package com.tuda.controller;

import com.tuda.service.EmailService;
import com.tuda.service.impl.EmailServiceImpl;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/sendQrEmail")
    public String sendQrEmail(
            @RequestParam String email,
            @RequestParam(required = false, defaultValue = "Тема письма") String subject,
            @RequestParam(required = false, defaultValue = "Приветствие") String text,
            @RequestParam(required = false, defaultValue = "Место для qr-кода") String qr
    ) throws MessagingException {
        emailService.sendQrEmail(email, subject, text, qr);
        return "Письмо отправлено на " + email + ". Проверьте MailHog: http://localhost:8025";
    }
}
