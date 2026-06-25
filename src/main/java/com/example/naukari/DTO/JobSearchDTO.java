package com.example.naukari.DTO;

import com.example.naukari.entity.JobType;
import lombok.Data;

@Data
public class JobSearchDTO {
    private String title;
    private String location;
    private String companyName;
    private JobType jobType;
    private Double minSalary;
    private String skills;
}
