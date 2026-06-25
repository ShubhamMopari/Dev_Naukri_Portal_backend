package com.example.naukari.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    private JobType jobType; // FULL_TIME, PART_TIME, INTERNSHIP, FREELANCE

    @Column(length = 5000)
    private String description;

    private String requiredSkills;
    private String requiredExperience;
    private String qualification;
    private Double minSalary;
    private Double maxSalary;
    private String salaryPeriod; // MONTHLY, YEARLY

    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.ACTIVE;

    @Column(updatable = false)
    private LocalDateTime postedAt = LocalDateTime.now();

    private LocalDateTime lastDateToApply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    private User employer;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Application> applications;
}
