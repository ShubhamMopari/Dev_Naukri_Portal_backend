package com.example.naukari.serviceimp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserServiceImp {

    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private SkillRepository skillRepo;
    @Autowired private ExperienceRepository experienceRepo;
    @Autowired private EducationRepository educationRepo;

    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + dto.getEmail());
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setQualification(dto.getQualification());
        user.setPhone(dto.getPhone());
        user.setLocation(dto.getLocation());
        if (dto.getRole() != null) user.setRole(dto.getRole());
        User saved = userRepo.save(user);
        return toResponse(saved);
    }

    public UserResponseDTO getUser(Integer id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return toResponse(user);
    }

    public UserResponseDTO updateProfile(String email, UpdateProfileDTO dto) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getLocation() != null) user.setLocation(dto.getLocation());
        if (dto.getProfileSummary() != null) user.setProfileSummary(dto.getProfileSummary());
        if (dto.getResumeLink() != null) user.setResumeLink(dto.getResumeLink());
        if (dto.getQualification() != null) user.setQualification(dto.getQualification());
        return toResponse(userRepo.save(user));
    }

    public String updatePassword(String email, UpdatePasswordDTO dto) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!passwordEncoder.matches(dto.getOldpassword(), user.getPassword())) {
            throw new InvalidInputException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewpassword()));
        userRepo.save(user);
        return "Password updated successfully";
    }

    // --- Skills ---
    public List<SkillDTO> addSkills(String email, List<SkillDTO> skillDTOs) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Skill> skills = skillDTOs.stream().map(dto -> {
            Skill s = new Skill();
            s.setSkillName(dto.getSkillName());
            s.setProficiencyLevel(dto.getProficiencyLevel());
            s.setUser(user);
            return s;
        }).collect(Collectors.toList());
        return skillRepo.saveAll(skills).stream().map(this::toSkillDTO).collect(Collectors.toList());
    }

    public void deleteSkill(String email, Integer skillId) {
        Skill skill = skillRepo.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
        if (!skill.getUser().getEmail().equals(email)) throw new UnauthorizedException("Not your skill");
        skillRepo.delete(skill);
    }

    public List<SkillDTO> getSkills(Integer userId) {
        return skillRepo.findByUserId(userId).stream().map(this::toSkillDTO).collect(Collectors.toList());
    }

    // --- Experience ---
    public ExperienceDTO addExperience(String email, ExperienceDTO dto) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Experience exp = new Experience();
        exp.setCompanyName(dto.getCompanyName());
        exp.setJobTitle(dto.getJobTitle());
        exp.setStartDate(dto.getStartDate());
        exp.setEndDate(dto.getEndDate());
        exp.setCurrentlyWorking(dto.isCurrentlyWorking());
        exp.setDescription(dto.getDescription());
        exp.setUser(user);
        return toExpDTO(experienceRepo.save(exp));
    }

    public void deleteExperience(String email, Integer expId) {
        Experience exp = experienceRepo.findById(expId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found"));
        if (!exp.getUser().getEmail().equals(email)) throw new UnauthorizedException("Not your experience");
        experienceRepo.delete(exp);
    }

    public List<ExperienceDTO> getExperiences(Integer userId) {
        return experienceRepo.findByUserId(userId).stream().map(this::toExpDTO).collect(Collectors.toList());
    }

    // --- Education ---
    public EducationDTO addEducation(String email, EducationDTO dto) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Education edu = new Education();
        edu.setInstitution(dto.getInstitution());
        edu.setDegree(dto.getDegree());
        edu.setFieldOfStudy(dto.getFieldOfStudy());
        edu.setStartDate(dto.getStartDate());
        edu.setEndDate(dto.getEndDate());
        edu.setPercentage(dto.getPercentage());
        edu.setUser(user);
        return toEduDTO(educationRepo.save(edu));
    }

    public void deleteEducation(String email, Integer eduId) {
        Education edu = educationRepo.findById(eduId)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found"));
        if (!edu.getUser().getEmail().equals(email)) throw new UnauthorizedException("Not your education");
        educationRepo.delete(edu);
    }

    public List<EducationDTO> getEducations(Integer userId) {
        return educationRepo.findByUserId(userId).stream().map(this::toEduDTO).collect(Collectors.toList());
    }

    // --- Mappers ---
    private UserResponseDTO toResponse(User user) {
        UserResponseDTO r = new UserResponseDTO();
        r.setId(user.getId());
        r.setName(user.getName());
        r.setEmail(user.getEmail());
        r.setQualification(user.getQualification());
        r.setPhone(user.getPhone());
        r.setLocation(user.getLocation());
        r.setProfileSummary(user.getProfileSummary());
        r.setResumeLink(user.getResumeLink());
        r.setRole(user.getRole());
        return r;
    }

    private SkillDTO toSkillDTO(Skill s) {
        SkillDTO d = new SkillDTO();
        d.setId(s.getId());
        d.setSkillName(s.getSkillName());
        d.setProficiencyLevel(s.getProficiencyLevel());
        return d;
    }

    private ExperienceDTO toExpDTO(Experience e) {
        ExperienceDTO d = new ExperienceDTO();
        d.setId(e.getId());
        d.setCompanyName(e.getCompanyName());
        d.setJobTitle(e.getJobTitle());
        d.setStartDate(e.getStartDate());
        d.setEndDate(e.getEndDate());
        d.setCurrentlyWorking(e.isCurrentlyWorking());
        d.setDescription(e.getDescription());
        return d;
    }

    private EducationDTO toEduDTO(Education e) {
        EducationDTO d = new EducationDTO();
        d.setId(e.getId());
        d.setInstitution(e.getInstitution());
        d.setDegree(e.getDegree());
        d.setFieldOfStudy(e.getFieldOfStudy());
        d.setStartDate(e.getStartDate());
        d.setEndDate(e.getEndDate());
        d.setPercentage(e.getPercentage());
        return d;
    }
}
