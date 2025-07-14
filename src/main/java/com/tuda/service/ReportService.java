package com.tuda.service;

import java.io.File;
import java.io.IOException;

public interface ReportService {
    File generateCsvReport(String filePath, Long eventId);
    byte[] generatePdfReport(Long eventId);
}
