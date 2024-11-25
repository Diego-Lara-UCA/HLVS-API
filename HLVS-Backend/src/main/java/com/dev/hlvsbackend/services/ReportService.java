package com.dev.hlvsbackend.services;

import com.dev.hlvsbackend.domain.dtos.Report.RegisterReportDTO;
import com.dev.hlvsbackend.domain.entities.Report;
import com.dev.hlvsbackend.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface ReportService {
    String createReport(RegisterReportDTO reportDTO, User user);
    List<Report> getReport();
    Report getReportById(UUID id);
    List<Report> getReportForGuards();
}
