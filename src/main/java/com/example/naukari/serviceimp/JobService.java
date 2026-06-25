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
public class JobService {

    @Autowired private JobRepository jobRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private ApplicationRepository appRepo;

    public JobResponseDTO createJob(String employerEmail, JobRequestDTO dto) {
        User employer = userRepo.findByEmail(employerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found"));
        if (employer.getRole() != Role.EMPLOYER && employer.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only employers can post jobs");
        }
        Job job = new Job();
        mapFromDTO(dto, job);
        job.setEmployer(employer);
        return toResponse(jobRepo.save(job), 0);
    }

    public JobResponseDTO updateJob(String employerEmail, Integer jobId, JobRequestDTO dto) {
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found: " + jobId));
        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new UnauthorizedException("You can only update your own jobs");
        }
        mapFromDTO(dto, job);
        long count = appRepo.findByJobId(jobId).size();
        return toResponse(jobRepo.save(job), count);
    }

    public void deleteJob(String employerEmail, Integer jobId) {
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found: " + jobId));
        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new UnauthorizedException("You can only delete your own jobs");
        }
        jobRepo.delete(job);
    }

    public void closeJob(String employerEmail, Integer jobId) {
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found: " + jobId));
        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new UnauthorizedException("You can only close your own jobs");
        }
        job.setStatus(JobStatus.CLOSED);
        jobRepo.save(job);
    }

    public JobResponseDTO getJob(Integer jobId) {
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found: " + jobId));
        long count = appRepo.findByJobId(jobId).size();
        return toResponse(job, count);
    }

    public List<JobResponseDTO> getAllActiveJobs() {
        return jobRepo.findByStatus(JobStatus.ACTIVE).stream()
                .map(j -> toResponse(j, appRepo.findByJobId(j.getId()).size()))
                .collect(Collectors.toList());
    }

    public List<JobResponseDTO> getMyJobs(String employerEmail) {
        User employer = userRepo.findByEmail(employerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return jobRepo.findByEmployerId(employer.getId()).stream()
                .map(j -> toResponse(j, appRepo.findByJobId(j.getId()).size()))
                .collect(Collectors.toList());
    }

    public List<JobResponseDTO> searchJobs(JobSearchDTO search) {
        return jobRepo.searchJobs(
                search.getTitle(),
                search.getLocation(),
                search.getCompanyName(),
                search.getJobType(),
                search.getMinSalary(),
                search.getSkills()
        ).stream()
                .map(j -> toResponse(j, appRepo.findByJobId(j.getId()).size()))
                .collect(Collectors.toList());
    }

    private void mapFromDTO(JobRequestDTO dto, Job job) {
        job.setTitle(dto.getTitle());
        job.setCompanyName(dto.getCompanyName());
        job.setLocation(dto.getLocation());
        job.setJobType(dto.getJobType());
        job.setDescription(dto.getDescription());
        job.setRequiredSkills(dto.getRequiredSkills());
        job.setRequiredExperience(dto.getRequiredExperience());
        job.setQualification(dto.getQualification());
        job.setMinSalary(dto.getMinSalary());
        job.setMaxSalary(dto.getMaxSalary());
        job.setSalaryPeriod(dto.getSalaryPeriod());
        job.setLastDateToApply(dto.getLastDateToApply());
    }

    private JobResponseDTO toResponse(Job job, long appCount) {
        JobResponseDTO r = new JobResponseDTO();
        r.setId(job.getId());
        r.setTitle(job.getTitle());
        r.setCompanyName(job.getCompanyName());
        r.setLocation(job.getLocation());
        r.setJobType(job.getJobType());
        r.setDescription(job.getDescription());
        r.setRequiredSkills(job.getRequiredSkills());
        r.setRequiredExperience(job.getRequiredExperience());
        r.setQualification(job.getQualification());
        r.setMinSalary(job.getMinSalary());
        r.setMaxSalary(job.getMaxSalary());
        r.setSalaryPeriod(job.getSalaryPeriod());
        r.setStatus(job.getStatus());
        r.setPostedAt(job.getPostedAt());
        r.setLastDateToApply(job.getLastDateToApply());
        r.setEmployerName(job.getEmployer().getName());
        r.setEmployerId(job.getEmployer().getId());
        r.setApplicationCount(appCount);
        return r;
    }
}
