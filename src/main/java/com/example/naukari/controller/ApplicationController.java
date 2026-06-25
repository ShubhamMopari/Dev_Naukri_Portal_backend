package com.example.naukari.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.naukari.DTO.*;
import com.example.naukari.serviceimp.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/applications")
@Tag(name = "Applications")
@SecurityRequirement(name = "bearerAuth")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/apply/{jobId}")
    @Operation(summary = "Apply for a job (JOBSEEKER only)")
    public ResponseEntity<ApplicationResponseDTO> applyForJob(
            @PathVariable Integer jobId,
            @RequestBody ApplicationRequestDTO dto,
            Principal principal) {
        return ResponseEntity.ok(applicationService.applyForJob(principal.getName(), jobId, dto));
    }

    @DeleteMapping("/{applicationId}/withdraw")
    @Operation(summary = "Withdraw your application")
    public ResponseEntity<String> withdraw(@PathVariable Integer applicationId, Principal principal) {
        applicationService.withdrawApplication(principal.getName(), applicationId);
        return ResponseEntity.ok("Application withdrawn");
    }

    @GetMapping("/my-applications")
    @Operation(summary = "Get all your applications (JOBSEEKER)")
    public ResponseEntity<List<ApplicationResponseDTO>> getMyApplications(Principal principal) {
        return ResponseEntity.ok(applicationService.getMyApplications(principal.getName()));
    }

    @GetMapping("/job/{jobId}")
    @Operation(summary = "Get all applications for a job (EMPLOYER only)")
    public ResponseEntity<List<ApplicationResponseDTO>> getApplicationsForJob(
            @PathVariable Integer jobId,
            Principal principal) {
        return ResponseEntity.ok(applicationService.getApplicationsForJob(principal.getName(), jobId));
    }

    @PutMapping("/{applicationId}/status")
    @Operation(summary = "Update application status — SHORTLISTED, REJECTED, HIRED (EMPLOYER)")
    public ResponseEntity<ApplicationResponseDTO> updateStatus(
            @PathVariable Integer applicationId,
            @RequestBody ApplicationStatusUpdateDTO dto,
            Principal principal) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(principal.getName(), applicationId, dto));
    }
}
