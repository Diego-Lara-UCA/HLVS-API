package com.dev.hlvsbackend.utils;

import com.dev.hlvsbackend.domain.dtos.Report.ResponseReportDTO;
import com.dev.hlvsbackend.domain.entities.Report;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReportUtils {
    public List<ResponseReportDTO> CreateListResponseReportDTO (List<Report> reports){
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

        return reportDTO;
    }
}
