package com.example.naukari.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.naukari.entity.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    List<Application> findByUserId(Integer userId);
    List<Application> findByJobId(Integer jobId);
    boolean existsByUserIdAndJobId(Integer userId, Integer jobId);
    Optional<Application> findByUserIdAndJobId(Integer userId, Integer jobId);
}
