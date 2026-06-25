package com.example.naukari.serviceimp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.naukari.DTO.*;
import com.example.naukari.entity.*;
import com.example.naukari.exception.*;
import com.example.naukari.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicationService {

    @Autowired private ApplicationRepository appRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private JobRepository jobRepo;

    public ApplicationResponseDTO applyForJob(String email, Integer jobId, ApplicationRequestDTO dto) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found: " + jobId));

        if (job.getStatus() != JobStatus.ACTIVE) {
            throw new InvalidInputException("This job is no longer accepting applications");
        }
        if (appRepo.existsByUserIdAndJobId(user.getId(), jobId)) {
            throw new InvalidInputException("You have already applied for this job");
        }

        Application app = new Application();
        app.setUser(user);
        app.setJob(job);
        app.setCoverLetter(dto.getCoverLetter());
        app.setResumeLink(dto.getResumeLink() != null ? dto.getResumeLink() : user.getResumeLink());

        return toResponse(appRepo.save(app));
    }

    public void withdrawApplication(String email, Integer applicationId) {
        Application app = appRepo.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        if (!app.getUser().getEmail().equals(email)) {
            throw new UnauthorizedException("This is not your application");
        }
        if (app.getStatus() != ApplicationStatus.APPLIED) {
            throw new InvalidInputException("Cannot withdraw application in status: " + app.getStatus());
        }
        appRepo.delete(app);
    }

    public List<ApplicationResponseDTO> getMyApplications(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return appRepo.findByUserId(user.getId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ApplicationResponseDTO> getApplicationsForJob(String employerEmail, Integer jobId) {
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new UnauthorizedException("You can only view applications for your own jobs");
        }
        return appRepo.findByJobId(jobId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ApplicationResponseDTO updateApplicationStatus(String employerEmail, Integer applicationId,
                                                          ApplicationStatusUpdateDTO dto) {
        Application app = appRepo.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        if (!app.getJob().getEmployer().getEmail().equals(employerEmail)) {
            throw new UnauthorizedException("You can only update applications for your own jobs");
        }
        app.setStatus(dto.getStatus());
        if (dto.getEmployerNote() != null) app.setEmployerNote(dto.getEmployerNote());
        return toResponse(appRepo.save(app));
    }

    private ApplicationResponseDTO toResponse(Application app) {
        ApplicationResponseDTO r = new ApplicationResponseDTO();
        r.setId(app.getId());
        r.setJobId(app.getJob().getId());
        r.setJobTitle(app.getJob().getTitle());
        r.setCompanyName(app.getJob().getCompanyName());
        r.setLocation(app.getJob().getLocation());
        r.setUserId(app.getUser().getId());
        r.setApplicantName(app.getUser().getName());
        r.setApplicantEmail(app.getUser().getEmail());
        r.setStatus(app.getStatus());
        r.setCoverLetter(app.getCoverLetter());
        r.setResumeLink(app.getResumeLink());
        r.setAppliedAt(app.getAppliedAt());
        r.setEmployerNote(app.getEmployerNote());
        return r;
    }
}
