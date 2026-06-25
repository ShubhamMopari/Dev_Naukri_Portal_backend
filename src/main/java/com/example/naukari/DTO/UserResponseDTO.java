package com.example.naukari.DTO;

import com.example.naukari.entity.Role;
import lombok.Data;

@Data
public class UserResponseDTO {
    private Integer id;
    private String name;
    private String email;
    private String qualification;
    private String phone;
    private String location;
    private String profileSummary;
    private String resumeLink;
    private Role role;
}
