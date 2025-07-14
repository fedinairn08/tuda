package com.tuda.service.impl;

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

            String[] header = {"ФИО", "Присутствие", "Роль", "Тип"};
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

}
