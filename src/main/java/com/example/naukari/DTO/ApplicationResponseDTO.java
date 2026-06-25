package com.example.naukari.DTO;

import java.time.LocalDateTime;
import com.example.naukari.entity.ApplicationStatus;
import lombok.Data;

@Data
public class ApplicationResponseDTO {
    private Integer id;
    private Integer jobId;
    private String jobTitle;
    private String companyName;
    private String location;
    private Integer userId;
    private String applicantName;
    private String applicantEmail;
    private ApplicationStatus status;
    private String coverLetter;
    private String resumeLink;
    private LocalDateTime appliedAt;
    private String employerNote;
}
