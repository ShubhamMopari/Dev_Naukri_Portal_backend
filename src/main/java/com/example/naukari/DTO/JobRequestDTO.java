package com.example.naukari.DTO;

import java.time.LocalDateTime;
import com.example.naukari.entity.JobType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class JobRequestDTO {
    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Location is required")
    private String location;

    private JobType jobType;

    @NotBlank(message = "Job description is required")
    private String description;

    private String requiredSkills;
    private String requiredExperience;
    private String qualification;
    private Double minSalary;
    private Double maxSalary;
    private String salaryPeriod;
    private LocalDateTime lastDateToApply;
}
