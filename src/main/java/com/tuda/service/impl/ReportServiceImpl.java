package com.tuda.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.tuda.data.enums.ParticipantType;
import com.tuda.data.enums.UserRole;
import com.tuda.dto.response.EventParticipantResponseDTO;
import com.tuda.service.EventService;
import com.tuda.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final EventService eventService;

    @Override
    public File generateCsvReport(String filePath, Long eventId) {
        List<EventParticipantResponseDTO> participants = eventService.getAllParticipantsByEventId(eventId);
        File file = new File(filePath);
        try (ICSVWriter writer = new CSVWriterBuilder(new FileWriter(file))
                .withSeparator(';')
                .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                .withEscapeChar('\\')
                .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                .build()) {

            String[] header = {"FullName", "Присутствие", "Роль", "Тип"};
            writer.writeNext(header);

            for (EventParticipantResponseDTO participant: participants) {

                String[] line = {
                        String.valueOf(participant.getFullName()),
                        participant.getStatus() ? "Пришёл" : "Пропустил",
                        participant.getRole().equals(UserRole.PARTICIPANT) ? "Участник" : "Волонтёр",
                        participant.getType().equals(ParticipantType.GUEST) ? "Гость" : "Пользователь"
                };
                writer.writeNext(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    @Override
    public byte[] generatePdfReport(Long eventId) {
        List<EventParticipantResponseDTO> participants = eventService.getAllParticipantsByEventId(eventId);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);

            // Регистрация шрифта с поддержкой кириллицы
            BaseFont bf = BaseFont.createFont(
                    "fonts/times-new-roman.ttf",
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );
            Font titleFont = new Font(bf, 16, Font.BOLD);
            Font headerFont = new Font(bf, 12, Font.BOLD);
            Font cellFont = new Font(bf, 10);

            document.open();

            // Заголовок
            Paragraph title = new Paragraph("Отчет о присутствующих на мероприятии", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // Таблица с данными
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3, 2, 2, 2});

            // Заголовки таблицы
            String[] headers = {"ФИО", "Присутствие", "Роль", "Тип"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Paragraph(header, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5f);
                table.addCell(cell);
            }

            // Данные участников
            for (EventParticipantResponseDTO participant : participants) {
                table.addCell(new Paragraph(participant.getFullName(), cellFont));
                table.addCell(new Paragraph(participant.getStatus() ? "Присутствовал" : "Отсутствовал", cellFont));
                table.addCell(new Paragraph(participant.getRole().equals(UserRole.PARTICIPANT) ? "Участник" : "Волонтер", cellFont));
                table.addCell(new Paragraph(participant.getType().equals(ParticipantType.GUEST) ? "Гость" : "Пользователь", cellFont));
            }

            document.add(table);
            document.close();
            return out.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Ошибка при генерации PDF", e);
        }
    }



}
