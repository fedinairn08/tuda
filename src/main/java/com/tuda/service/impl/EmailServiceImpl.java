package com.tuda.service.impl;

import com.tuda.exception.BadRequestException;
import com.tuda.exception.EmailException;
import com.tuda.exception.QrCodeGenerationException;
import com.tuda.service.EmailService;
import com.tuda.service.QrCodeService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final QrCodeService qrCodeService;

    @Override
    public void sendQrEmail(String toEmail, String subject, String text, String key) {
        if (toEmail == null || !toEmail.contains("@")) {
            throw new BadRequestException("Некорректный email адрес: " + toEmail);
        }
        if (key == null || key.isBlank()) {
            throw new BadRequestException("Ключ для QR-кода не может быть пустым");
        }

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String qrCodeBase64;
            try {
                qrCodeBase64 = qrCodeService.generateQrCode(key);
            } catch (Exception e) {
                throw new QrCodeGenerationException("Не удалось сгенерировать QR-код");
            }

            String htmlContent = String.format("""
            <h3>%s</h3>
            <p>Ваш QR-код для мероприятия:</p>
            <img src='data:image/png;base64,%s' alt='QR Code'/>
            """, text, qrCodeBase64);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            try {
                mailSender.send(message);
            } catch (MailException e) {
                throw new EmailException("Ошибка при отправке email " + e);
            }

        } catch (MessagingException e) {
            throw new EmailException("Ошибка при создании email сообщения");
        }
    }

}
