package com.example.naukari.DTO;

import com.example.naukari.entity.ApplicationStatus;
import lombok.Data;

@Data
public class ApplicationStatusUpdateDTO {
    private ApplicationStatus status;
    private String employerNote;
}
