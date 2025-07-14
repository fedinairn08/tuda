package com.tuda.controller;

import com.tuda.service.EventService;
import com.tuda.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

@RestController
@AllArgsConstructor
@SecurityRequirement(name = "JWT")
@RequestMapping("/report")
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

    @Operation(
            summary = "Download PDF report",
            description = "Generates and downloads a PDF report for the specified event",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "PDF report file",
                            content = @Content(
                                    mediaType = "application/pdf",
                                    schema = @Schema(type = "string", format = "binary") // Важно: указываем binary!
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Event not found",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    )
            }
    )
    @GetMapping("/pdf/download")
    public ResponseEntity<Resource> getPdfReport(@RequestParam Long eventId) {
        byte[] pdfBytes = reportService.generatePdfReport(eventId);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=event_participants_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(resource);
    }
}
