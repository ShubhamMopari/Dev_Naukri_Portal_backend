package com.example.naukari.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.naukari.entity.Experience;

public interface ExperienceRepository extends JpaRepository<Experience, Integer> {
    List<Experience> findByUserId(Integer userId);
}
