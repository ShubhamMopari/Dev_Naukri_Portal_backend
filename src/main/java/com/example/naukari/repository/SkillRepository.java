package com.example.naukari.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.naukari.entity.Skill;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
    List<Skill> findByUserId(Integer userId);
    void deleteByUserId(Integer userId);
}
