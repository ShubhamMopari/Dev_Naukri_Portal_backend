package com.example.naukari.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.naukari.DTO.*;
import com.example.naukari.serviceimp.UserServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@Tag(name = "User & Profile")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserServiceImp userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user (JOBSEEKER or EMPLOYER)")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @GetMapping("/profile/{id}")
    @Operation(summary = "Get user profile by ID")
    public ResponseEntity<UserResponseDTO> getProfile(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update your profile")
    public ResponseEntity<UserResponseDTO> updateProfile(@RequestBody UpdateProfileDTO dto, Principal principal) {
        return ResponseEntity.ok(userService.updateProfile(principal.getName(), dto));
    }

    @PutMapping("/update-password")
    @Operation(summary = "Update your password")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordDTO dto, Principal principal) {
        return ResponseEntity.ok(userService.updatePassword(principal.getName(), dto));
    }

    // --- Skills ---
    @PostMapping("/skills")
    @Operation(summary = "Add skills to your profile")
    public ResponseEntity<List<SkillDTO>> addSkills(@RequestBody List<SkillDTO> skills, Principal principal) {
        return ResponseEntity.ok(userService.addSkills(principal.getName(), skills));
    }

    @GetMapping("/{userId}/skills")
    @Operation(summary = "Get skills of a user")
    public ResponseEntity<List<SkillDTO>> getSkills(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getSkills(userId));
    }

    @DeleteMapping("/skills/{skillId}")
    @Operation(summary = "Delete a skill")
    public ResponseEntity<String> deleteSkill(@PathVariable Integer skillId, Principal principal) {
        userService.deleteSkill(principal.getName(), skillId);
        return ResponseEntity.ok("Skill deleted");
    }

    // --- Experience ---
    @PostMapping("/experience")
    @Operation(summary = "Add work experience")
    public ResponseEntity<ExperienceDTO> addExperience(@RequestBody ExperienceDTO dto, Principal principal) {
        return ResponseEntity.ok(userService.addExperience(principal.getName(), dto));
    }

    @GetMapping("/{userId}/experience")
    @Operation(summary = "Get work experience of a user")
    public ResponseEntity<List<ExperienceDTO>> getExperiences(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getExperiences(userId));
    }

    @DeleteMapping("/experience/{expId}")
    @Operation(summary = "Delete a work experience entry")
    public ResponseEntity<String> deleteExperience(@PathVariable Integer expId, Principal principal) {
        userService.deleteExperience(principal.getName(), expId);
        return ResponseEntity.ok("Experience deleted");
    }

    // --- Education ---
    @PostMapping("/education")
    @Operation(summary = "Add education")
    public ResponseEntity<EducationDTO> addEducation(@RequestBody EducationDTO dto, Principal principal) {
        return ResponseEntity.ok(userService.addEducation(principal.getName(), dto));
    }

    @GetMapping("/{userId}/education")
    @Operation(summary = "Get education of a user")
    public ResponseEntity<List<EducationDTO>> getEducations(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getEducations(userId));
    }

    @DeleteMapping("/education/{eduId}")
    @Operation(summary = "Delete education entry")
    public ResponseEntity<String> deleteEducation(@PathVariable Integer eduId, Principal principal) {
        userService.deleteEducation(principal.getName(), eduId);
        return ResponseEntity.ok("Education deleted");
    }
}
