package com.example.naukari.DTO;

import java.time.LocalDate;
import lombok.Data;

@Data
public class EducationDTO {
    private Integer id;
    private String institution;
    private String degree;
    private String fieldOfStudy;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double percentage;
}
