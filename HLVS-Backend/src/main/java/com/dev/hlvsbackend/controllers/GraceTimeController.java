package com.dev.hlvsbackend.controllers;

import com.dev.hlvsbackend.domain.dtos.GeneralResponse;
import com.dev.hlvsbackend.domain.dtos.GraceTime.UpdateGraceTimeDTO;
import com.dev.hlvsbackend.services.GraceTimeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grace-time")
public class GraceTimeController {
    private final GraceTimeService graceTimeService;

    public GraceTimeController(GraceTimeService graceTimeService){
        this.graceTimeService = graceTimeService;
    }

    @PutMapping("update")
    public ResponseEntity<GeneralResponse> CreateKey(@RequestBody @Valid UpdateGraceTimeDTO data, BindingResult error){
        if (error.hasErrors()){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating user",
                    error.getAllErrors()
            );
        }

        try {
            graceTimeService.UpdateGraceTime(data);

            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    "Grace time updated!"
            );

        }catch (Exception e){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when getting data",
                    e.getMessage()
            );
        }
    }
}
