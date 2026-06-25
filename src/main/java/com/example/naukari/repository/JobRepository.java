package com.example.naukari.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.naukari.entity.Job;
import com.example.naukari.entity.JobStatus;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {

    List<Job> findByStatus(JobStatus status);

    List<Job> findByEmployerId(Integer employerId);

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' AND " +
           "(:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:companyName IS NULL OR LOWER(j.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))) AND " +
           "(:jobType IS NULL OR j.jobType = :jobType) AND " +
           "(:minSalary IS NULL OR j.minSalary >= :minSalary) AND " +
           "(:skills IS NULL OR LOWER(j.requiredSkills) LIKE LOWER(CONCAT('%', :skills, '%')))")
    List<Job> searchJobs(@Param("title") String title,
                         @Param("location") String location,
                         @Param("companyName") String companyName,
                         @Param("jobType") com.example.naukari.entity.JobType jobType,
                         @Param("minSalary") Double minSalary,
                         @Param("skills") String skills);
}
