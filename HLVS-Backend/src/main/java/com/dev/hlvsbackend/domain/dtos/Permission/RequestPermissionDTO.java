package com.dev.hlvsbackend.domain.dtos.Permission;

import lombok.Data;

import java.util.List;

@Data
public class RequestPermissionDTO {
    private String email_house;
    private String email_permission;
    private List<String> days;
    private String firstDate;
    private String secondDate;
    private String initialHour;
    private String finalHour;
    private String expirationType;
}
