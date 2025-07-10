package com.tuda.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendQrEmail(String toEmail, String subject, String text, String qrCodeBase64) throws MessagingException;
}
