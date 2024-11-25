package com.dev.hlvsbackend.controllers;

import com.dev.hlvsbackend.domain.dtos.GeneralResponse;
import com.dev.hlvsbackend.domain.dtos.Report.RegisterReportDTO;
import com.dev.hlvsbackend.domain.dtos.Report.ResponseReportDTO;
import com.dev.hlvsbackend.domain.entities.Report;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.repositories.UserRepository;
import com.dev.hlvsbackend.services.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    private final ReportService reportService;
    private final UserRepository userRepository;

    public ReportController(
            ReportService reportService,
            UserRepository userRepository) {
        this.reportService = reportService;
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<GeneralResponse> getAllReports() {
        List<Report> reports = reportService.getReport();
        List<ResponseReportDTO> reportDTO = new ArrayList<>();

        reports.forEach(report -> {
            ResponseReportDTO dto = new ResponseReportDTO();
            dto.setId(report.getId());
            dto.setNombre(report.getUser().getNombre());
            dto.setDescription(report.getDescription());
            dto.setType(String.valueOf(report.getType()));
            dto.setDate(report.getCreatedAt());
            reportDTO.add(dto);
        });
        return GeneralResponse.getResponse(
                HttpStatus.OK,
                "List of all reports!",
                reportDTO
        );
    }
            /*if(reports.isEmpty()) {
                return GeneralResponse.getResponse(
                        HttpStatus.NO_CONTENT,
                        "No reports found"
                );
            }
        } catch (Exception e) {
            return GeneralResponse.getResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error fetching reports",
                    e.getMessage()
            );
        }
    }*/

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse> createReport(
            @RequestBody @Valid RegisterReportDTO data, BindingResult error, User user)
    {
        if (error.hasErrors()) {
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating report",
                    error.getAllErrors()
            );
        }
        try {
            String response = reportService.createReport(data, user);

            return GeneralResponse.getResponse(
                    HttpStatus.CREATED,
                    "Report created successfully",
                    response
            );
        } catch (Exception e) {
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error creating report",
                    e.getMessage()
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getReportById(@PathVariable UUID id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            List<Report> reports = user.getReports();

            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    "Lista de reportes del usuario " + user.getNombre(),
                    reports
            );
        } catch (Exception e) {
            return GeneralResponse.getResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Error al obtener los reportes",
            e.getMessage()
            );
        }
        /*try {
            Report report = reportService.getReportById(id);

            if(report == null) {
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "Report not found with ID: " + id
                );
            }
            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    "Report retrieved successfully",
                    report
            );
        } catch (Exception e) {
            return GeneralResponse.getResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error fetching report",
                    e.getMessage()
            );
        }*/
    }

    @GetMapping("/guard")
    public ResponseEntity<GeneralResponse> getReportGuard() {
        List<Report> reports = reportService.getReportForGuards();
        List<ResponseReportDTO> reportDTO = new ArrayList<>();

        reports.forEach(report -> {
            ResponseReportDTO dto = new ResponseReportDTO();
            dto.setId(report.getId());
            dto.setNombre(report.getUser().getNombre());
            dto.setDescription(report.getDescription());
            dto.setDate(report.getCreatedAt());
            reportDTO.add(dto);
        });
        return GeneralResponse.getResponse(
                HttpStatus.OK,
                "Reports for guards retrieved successfully",
                reportDTO
        );
    }

}
