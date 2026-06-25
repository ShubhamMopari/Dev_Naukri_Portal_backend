package com.example.naukari.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.naukari.DTO.*;
import com.example.naukari.serviceimp.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/jobs")
@Tag(name = "Jobs")
@SecurityRequirement(name = "bearerAuth")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    @Operation(summary = "Post a new job (EMPLOYER only)")
    public ResponseEntity<JobResponseDTO> createJob(@Valid @RequestBody JobRequestDTO dto, Principal principal) {
        return ResponseEntity.ok(jobService.createJob(principal.getName(), dto));
    }

    @PutMapping("/{jobId}")
    @Operation(summary = "Update your job posting")
    public ResponseEntity<JobResponseDTO> updateJob(@PathVariable Integer jobId,
                                                     @RequestBody JobRequestDTO dto,
                                                     Principal principal) {
        return ResponseEntity.ok(jobService.updateJob(principal.getName(), jobId, dto));
    }

    @DeleteMapping("/{jobId}")
    @Operation(summary = "Delete your job posting")
    public ResponseEntity<String> deleteJob(@PathVariable Integer jobId, Principal principal) {
        jobService.deleteJob(principal.getName(), jobId);
        return ResponseEntity.ok("Job deleted");
    }

    @PutMapping("/{jobId}/close")
    @Operation(summary = "Close a job (stop accepting applications)")
    public ResponseEntity<String> closeJob(@PathVariable Integer jobId, Principal principal) {
        jobService.closeJob(principal.getName(), jobId);
        return ResponseEntity.ok("Job closed");
    }

    @GetMapping("/{jobId}")
    @Operation(summary = "Get job details by ID")
    public ResponseEntity<JobResponseDTO> getJob(@PathVariable Integer jobId) {
        return ResponseEntity.ok(jobService.getJob(jobId));
    }

    @GetMapping
    @Operation(summary = "Get all active jobs")
    public ResponseEntity<List<JobResponseDTO>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllActiveJobs());
    }

    @GetMapping("/my-jobs")
    @Operation(summary = "Get jobs posted by current employer")
    public ResponseEntity<List<JobResponseDTO>> getMyJobs(Principal principal) {
        return ResponseEntity.ok(jobService.getMyJobs(principal.getName()));
    }

    @GetMapping("/search")
    @Operation(summary = "Search jobs by title, location, skills, salary, type (all optional)")
    public ResponseEntity<List<JobResponseDTO>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) com.example.naukari.entity.JobType jobType,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) String skills) {

        JobSearchDTO search = new JobSearchDTO();
        search.setTitle(title);
        search.setLocation(location);
        search.setCompanyName(companyName);
        search.setJobType(jobType);
        search.setMinSalary(minSalary);
        search.setSkills(skills);
        return ResponseEntity.ok(jobService.searchJobs(search));
    }
}
