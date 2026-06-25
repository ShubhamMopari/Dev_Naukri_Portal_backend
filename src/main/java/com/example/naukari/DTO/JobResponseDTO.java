package com.example.naukari.DTO;

import java.time.LocalDateTime;
import com.example.naukari.entity.JobStatus;
import com.example.naukari.entity.JobType;
import lombok.Data;

@Data
public class JobResponseDTO {
    private Integer id;
    private String title;
    private String companyName;
    private String location;
    private JobType jobType;
    private String description;
    private String requiredSkills;
    private String requiredExperience;
    private String qualification;
    private Double minSalary;
    private Double maxSalary;
    private String salaryPeriod;
    private JobStatus status;
    private LocalDateTime postedAt;
    private LocalDateTime lastDateToApply;
    private String employerName;
    private Integer employerId;
    private long applicationCount;
}
