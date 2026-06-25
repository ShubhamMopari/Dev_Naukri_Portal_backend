package com.example.naukari.DTO;

import lombok.Data;

@Data
public class UpdateProfileDTO {
    private String name;
    private String phone;
    private String location;
    private String profileSummary;
    private String resumeLink;
    private String qualification;
}
