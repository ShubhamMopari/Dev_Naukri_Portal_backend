package com.example.naukari.DTO;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ExperienceDTO {
    private Integer id;
    private String companyName;
    private String jobTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean currentlyWorking;
    private String description;
}
