package com.dev.hlvsbackend.controllers;

import com.dev.hlvsbackend.domain.dtos.GeneralResponse;
import com.dev.hlvsbackend.domain.dtos.Report.RegisterReportDTO;
import com.dev.hlvsbackend.domain.dtos.Report.ResponseReportDTO;
import com.dev.hlvsbackend.domain.entities.Report;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.repositories.UserRepository;
import com.dev.hlvsbackend.services.ReportService;
import com.dev.hlvsbackend.services.UserService;
import com.dev.hlvsbackend.utils.ReportUtils;
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
    private final UserService userService;
    private final ReportUtils reportUtils;

    public ReportController(
            ReportService reportService,
            UserService userService, ReportUtils reportUtils
    ){
        this.reportService = reportService;
        this.userService = userService;
        this.reportUtils = reportUtils;
    }

    @GetMapping("/all")
    public ResponseEntity<GeneralResponse> getAllReports() {
        List<Report> reports = reportService.getReport();
        List<ResponseReportDTO> reportDTO = reportUtils.CreateListResponseReportDTO(reports);
        return GeneralResponse.getResponse(
                HttpStatus.OK,
                "List of all reports!",
                reportDTO
        );
    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse> createReport(@RequestBody @Valid RegisterReportDTO data, BindingResult error)
    {
        if (error.hasErrors()) {
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating report",
                    error.getAllErrors()
            );
        }
        try {
            User user = userService.getUserByEmail(data.getEmail());
            if (user == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "User not found: " + data.getEmail()
                );
            }
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
    public ResponseEntity<GeneralResponse> getReportById(@PathVariable String id) {
        try {
            User user = userService.getUserById(id);
            if (user == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "User not found: " + id
                );
            }

            List<ResponseReportDTO> response = reportUtils.CreateListResponseReportDTO(user.getReports());
            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    "Lista de reportes del usuario " + user.getNombre(),
                    response
            );
        } catch (Exception e) {
            return GeneralResponse.getResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Error al obtener los reportes",
            e.getMessage()
            );
        }
    }

    @GetMapping("/guard")
    public ResponseEntity<GeneralResponse> getReportGuard() {
        List<Report> reports = reportService.getReportForGuards();

        if (reports.isEmpty()){
            return GeneralResponse.getResponse(
                    HttpStatus.NOT_FOUND,
                    "No reports found!"
            );
        }

        List<ResponseReportDTO> reportDTO = reportUtils.CreateListResponseReportDTO(reports);
        return GeneralResponse.getResponse(
                HttpStatus.OK,
                "Reports for guards retrieved successfully",
                reportDTO
        );
    }
}
