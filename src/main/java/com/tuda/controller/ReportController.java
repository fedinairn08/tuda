package com.tuda.controller;

import com.tuda.data.entity.Event;
import com.tuda.dto.ApiResponse;
import com.tuda.dto.response.EventResponseDTO;
import com.tuda.service.EventService;
import com.tuda.service.ReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/report")
@SecurityRequirement(name = "JWT")
public class ReportController {
    private final Clock clock;
    private final ReportService reportService;

    @GetMapping("/cvs/download")
    public ResponseEntity<Resource> getCvsReport(@RequestParam Long eventId) throws IOException {
        String timestamp = LocalDateTime.now(clock).format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "event_report_%s.csv".formatted(timestamp);
        File reportFile = reportService.generateCsvReport(System.getProperty("java.io.tmpdir") + File.separator + filename, eventId);

        FileSystemResource resource = new FileSystemResource(reportFile);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentLength(Files.size(reportFile.toPath()))
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }
}
