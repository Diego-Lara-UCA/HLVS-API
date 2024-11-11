package com.dev.hlvsbackend.domain.dtos.EntranceKey;

import lombok.Data;

@Data
public class EntranceKeyResponseDTO {
    private String key;
    private String graceTime;
    private String creationDate;
    private String creationTime;
}
