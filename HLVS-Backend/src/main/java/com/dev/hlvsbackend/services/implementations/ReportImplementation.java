package com.dev.hlvsbackend.services.implementations;


import com.dev.hlvsbackend.domain.dtos.Report.RegisterReportDTO;
import com.dev.hlvsbackend.domain.entities.Report;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.repositories.ReportRepository;
import com.dev.hlvsbackend.repositories.UserRepository;
import com.dev.hlvsbackend.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReportImplementation implements ReportService {
    @Autowired
    private ReportRepository reportRepository;

    @Override
    public String createReport(RegisterReportDTO reportDTO, User user) {
        Report report = new Report();
        report.setDescription(reportDTO.getDescription());
        report.setType(reportDTO.getType());
        report.setCreatedAt(LocalDateTime.now());
        report.setUser(user);

        reportRepository.save(report);
        return "Report successful!";
    }

    @Override
    public List<Report> getReport() {
        return reportRepository.findAll();
    }

    @Override
    public List<Report> getReportForGuards() {
        return reportRepository.findAll();
    }

    @Override
    public List<Report> ReportListById(User user) {
        return reportRepository.findByUser(user);
    }

    @Override
    public Report getReportById(UUID id) {
        return reportRepository
                .findById(id)
                .orElse(null);
    }
}