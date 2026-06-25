package com.example.naukari.DTO;

import com.example.naukari.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String qualification;
    private String phone;
    private String location;
    private Role role = Role.JOBSEEKER;
}
