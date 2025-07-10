package com.tuda.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.tuda.exception.QrCodeGenerationException;
import com.tuda.service.QrCodeService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class QrCodeServiceImpl implements QrCodeService {

    @Override
    public String generateQrCode(String data) {
        if (data == null || data.isBlank()) {
            throw new IllegalArgumentException("Данные для QR-кода не могут быть пустыми");
        }

        final int width = 300;
        final int height = 300;

        try {
            BitMatrix matrix = new MultiFormatWriter().encode(
                    data,
                    BarcodeFormat.QR_CODE,
                    width,
                    height
            );

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                MatrixToImageWriter.writeToStream(matrix, "PNG", out);
                return Base64.getEncoder().encodeToString(out.toByteArray());
            }
        } catch (WriterException e) {
            throw new QrCodeGenerationException("Ошибка генерации QR-кода: " + e.getMessage());
        } catch (IOException e) {
            throw new QrCodeGenerationException("Ошибка преобразования в Base64: " + e.getMessage());
        }
    }


}
