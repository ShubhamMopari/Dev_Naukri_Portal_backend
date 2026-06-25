package com.example.naukari.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.naukari.entity.Education;

public interface EducationRepository extends JpaRepository<Education, Integer> {
    List<Education> findByUserId(Integer userId);
}
