package com.example.naukari.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdatePasswordDTO {
    @NotBlank
    private String oldpassword;
    @NotBlank
    @Size(min = 6)
    private String newpassword;
}
