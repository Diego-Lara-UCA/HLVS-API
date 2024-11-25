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

    //@Autowired
    //private UserRepository userRepository;

    @Override
    public String createReport(RegisterReportDTO reportDTO, User user) {
        Report report = new Report();
        report.setDescription(reportDTO.getDescription());
        report.setType(reportDTO.getType());
        report.setCreatedAt(LocalDateTime.now());
        report.setUser(user);

        reportRepository.save(report);
        return "Report successful!";

        /*
        // Buscar usuario por ID proporcionado
        User users = userRepository.findById(reportDTO.getIdUser()).orElse(null);

        if (users == null) {
            throw new IllegalArgumentException("User not found with ID: " + reportDTO.getIdUser());
        }

        // Crear y asignar valores al reporte
        Report report = new Report();
        report.setUser(users); // Asociar el usuario al reporte
        report.setDescription(reportDTO.getDescription());
        report.setType(reportDTO.getType());
        report.setCreatedAt(LocalDateTime.now()); // Asignar fecha de creación

        // Guardar el reporte en la base de datos
        reportRepository.save(report);

        return "Report created successfully";*/
    }

    @Override
    public List<Report> getReport() {
        return reportRepository.findAll();
    }
    @Override
    public List<Report> getReportForGuards() {
        return reportRepository.findAll();
    }
    /* @Override
    public List<Report> ReportListById(UUID userId) {
        return reportRepository.findByUser_Id(userId);
    }*/
    @Override
    public Report getReportById(UUID id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
    }
}