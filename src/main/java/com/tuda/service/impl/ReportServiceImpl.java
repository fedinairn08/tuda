package com.tuda.service.impl;

import com.itextpdf.text.*;
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
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Paragraph title = new Paragraph("Отчет о присутствующих", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setWidths(new float[]{3, 3, 3, 3});  // Ширина колонок

            String[] headers = {"FullName", "Присутствие", "Роль", "Тип"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Paragraph(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5f);
                table.addCell(cell);
            }

            for (EventParticipantResponseDTO participant : participants) {
                addTableRow(table, participant.getFullName());
                addTableRow(table, participant.getStatus() ? "Пришёл" : "Пропустил");
                addTableRow(table,  participant.getRole().equals(UserRole.PARTICIPANT) ? "Участник" : "Волонтёр");
                addTableRow(table, participant.getType().equals(ParticipantType.GUEST) ? "Гость" : "Пользователь");
            }

            document.add(table);
            document.close();

            return out.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Ошибка при генерации PDF", e);
        }
    }

    private void addTableRow(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Paragraph(text));
        table.addCell(cell);
    }

}
