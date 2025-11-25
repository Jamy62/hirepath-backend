package com.hirepath.hirepath_backend.service.user.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.company.CompanyListDTO;
import com.hirepath.hirepath_backend.model.dto.education.EducationListDTO;
import com.hirepath.hirepath_backend.model.dto.preferredindustry.PreferredIndustryListDTO;
import com.hirepath.hirepath_backend.model.dto.skill.SkillListDTO;
import com.hirepath.hirepath_backend.model.dto.user.UserDetailDTO;
import com.hirepath.hirepath_backend.model.dto.user.UserListDTO;
import com.hirepath.hirepath_backend.model.dto.user.UserListProjection;
import com.hirepath.hirepath_backend.model.dto.user.UserProfileDTO;
import com.hirepath.hirepath_backend.model.dto.userlanguage.UserLanguageListDTO;
import com.hirepath.hirepath_backend.model.dto.workexperience.WorkExperienceListDTO;
import com.hirepath.hirepath_backend.model.entity.companyuser.CompanyUser;
import com.hirepath.hirepath_backend.model.entity.education.Education;
import com.hirepath.hirepath_backend.model.entity.preferredindustry.PreferredIndustry;
import com.hirepath.hirepath_backend.model.entity.preferredlanguage.PreferredLanguage;
import com.hirepath.hirepath_backend.model.entity.role.Role;
import com.hirepath.hirepath_backend.model.entity.skill.Skill;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.entity.userlanguage.UserLanguage;
import com.hirepath.hirepath_backend.model.entity.workexperience.WorkExperience;
import com.hirepath.hirepath_backend.model.request.user.RegisterRequest;
import com.hirepath.hirepath_backend.model.request.user.UserUpdateRequest;
import com.hirepath.hirepath_backend.repository.companyuser.CompanyUserRepository;
import com.hirepath.hirepath_backend.repository.education.EducationRepository;
import com.hirepath.hirepath_backend.repository.preferredindustry.PreferredIndustryRepository;
import com.hirepath.hirepath_backend.repository.preferredlanguage.PreferredLanguageRepository;
import com.hirepath.hirepath_backend.repository.role.RoleRepository;
import com.hirepath.hirepath_backend.repository.skill.SkillRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.repository.userlanguage.UserLanguageRepository;
import com.hirepath.hirepath_backend.repository.workexperience.WorkExperienceRepository;
import com.hirepath.hirepath_backend.service.role.RoleService;
import com.hirepath.hirepath_backend.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final PreferredLanguageRepository preferredLanguageRepository;
    private final EducationRepository educationRepository;
    private final SkillRepository skillRepository;
    private final WorkExperienceRepository workExperienceRepository;
    private final PreferredIndustryRepository preferredIndustryRepository;
    private final UserLanguageRepository userLanguageRepository;
    private final CompanyUserRepository companyUserRepository;

    public User findByGuid(String guid) {
        try {
            return userRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    public User findByEmail(String email) {
        try {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    public String register(RegisterRequest request, String userType) {
        try {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This email already has an account");
            }

            Role role = roleRepository.findByName(userType.equals("admin") ? VariableConstant.ADMIN : VariableConstant.USER)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found"));
            PreferredLanguage preferredLanguage = preferredLanguageRepository.findByName("English")
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Preferred language not found"));
            User user = User.builder()
                    .role(role)
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .name(request.getName())
                    .fullName(request.getFullName())
                    .preferredLanguage(preferredLanguage)
                    .isActive(true)
                    .isDeleted(false)
                    .isBlocked(false)
                    .guid(UUID.randomUUID().toString())
                    .createdAt(ZonedDateTime.now())
                    .build();

            userRepository.save(user);

            return user.getName();
        }
        catch (Exception e) {
            throw e;
        }
    }

    public List<UserListDTO> userList(String searchName, String orderBy, String role, int first, int max) {
        try {
            if (searchName.isBlank()) {
                searchName = null;
            }

            if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
                List<UserListProjection> userListProjection = userRepository.findAllUsersAdminPanal(searchName, orderBy, role, first, max);

                return userListProjection.stream()
                        .map(p -> UserListDTO.builder()
                                .name(p.getName())
                                .fullName(p.getFullName())
                                .email(p.getEmail())
                                .mobile(p.getMobile())
                                .profile(p.getProfile())
                                .roleName(p.getRoleName())
                                .isActive(p.getIsActive())
                                .isBlocked(p.getIsBlocked())
                                .guid(p.getGuid())
                                .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .updatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .lastLoginAt(p.getLastLoginAt() != null ? p.getLastLoginAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .build())
                        .toList();
            }

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "please enter either ASC or DESC for orderBy");
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String userUpdate(String userGuid, UserUpdateRequest request, String email) {
        try {
            log.info("User update initiate");
            User user = findByGuid(userGuid);
            User updatedBy = findByEmail(email);

            if (request.getName() != null && !request.getName().isBlank()) {
                user.setName(request.getName());
            }
            if (request.getFullName() != null && !request.getFullName().isBlank()) {
                user.setFullName(request.getFullName());
            }
            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                user.setEmail(request.getEmail());
            }
            if (request.getMobile() != null && !request.getMobile().isBlank()) {
                user.setMobile(request.getMobile());
            }
            if (request.getRoleGuid() != null && !request.getRoleGuid().isBlank()) {
                Role role = roleService.findByGuid(request.getRoleGuid());
                user.setRole(role);
            }
            if (request.getIsActive() instanceof Boolean) {
                user.setIsActive(request.getIsActive());
            }
            if (request.getIsBlocked() instanceof Boolean) {
                user.setIsBlocked(request.getIsBlocked());
            }

            user.setUpdatedAt(ZonedDateTime.now());
            user.setUpdatedBy(updatedBy.getId());
            log.info(String.valueOf(user.getCreatedAt()));
            userRepository.save(user);

            return user.getGuid();
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String userDelete(String guid, String email) {
        try {
            User user = findByGuid(guid);
            User deletedBy = findByEmail(email);

            user.setIsDeleted(true);
            user.setUpdatedAt(ZonedDateTime.now());
            user.setUpdatedBy(deletedBy.getId());
            userRepository.save(user);

            return guid;
        }
        catch (Exception e) {
            throw e;
        }
    }

    public UserDetailDTO userDetail(String userGuid) {
        try {
            User user = findByGuid(userGuid);
            List<Education> educationList = educationRepository.findAllByUserAndIsDeletedFalse(user);
            List<Skill> skillList = skillRepository.findAllByUserAndIsDeletedFalse(user);
            List<WorkExperience> workExperienceList = workExperienceRepository.findAllByUserAndIsDeletedFalse(user);
            List<PreferredIndustry> preferredIndustryList = preferredIndustryRepository.findAllByUserAndIsDeletedFalse(user);
            List<UserLanguage> userLanguageList = userLanguageRepository.findAllByUserAndIsDeletedFalse(user);

            List<EducationListDTO> educationListDTOs = educationList.stream()
                    .map(e -> EducationListDTO.builder()
                            .institution(e.getInstitution())
                            .degree(e.getDegree())
                            .startDate(e.getStartDate())
                            .endDate(e.getEndDate())
                            .guid(e.getGuid())
                            .createdAt(e.getCreatedAt())
                            .updatedAt(e.getUpdatedAt())
                            .build()).toList();

            List<SkillListDTO> skillListDTOs = skillList.stream()
                    .map(s -> SkillListDTO.builder()
                            .name(s.getName())
                            .proficiency(s.getProficiency())
                            .guid(s.getGuid())
                            .createdAt(s.getCreatedAt())
                            .updatedAt(s.getUpdatedAt())
                            .build()).toList();

            List<WorkExperienceListDTO> workExperienceListDTOs = workExperienceList.stream()
                    .map(w -> WorkExperienceListDTO.builder()
                            .companyName(w.getCompanyName())
                            .position(w.getPosition())
                            .startDate(w.getStartDate())
                            .endDate(w.getEndDate())
                            .description(w.getDescription())
                            .guid(w.getGuid())
                            .createdAt(w.getCreatedAt())
                            .updatedAt(w.getUpdatedAt())
                            .build()).toList();

            List<PreferredIndustryListDTO> preferredIndustryListDTOs = preferredIndustryList.stream()
                    .map(p -> PreferredIndustryListDTO.builder()
                            .guid(p.getGuid())
                            .industryName(p.getIndustry().getName())
                            .industryGuid(p.getIndustry().getGuid())
                            .createdAt(p.getCreatedAt())
                            .updatedAt(p.getUpdatedAt())
                            .build()).toList();

            List<UserLanguageListDTO> userLanguageListDTOs = userLanguageList.stream()
                    .map(u -> UserLanguageListDTO.builder()
                            .guid(u.getGuid())
                            .languageName(u.getLanguage().getName())
                            .languageCode(u.getLanguage().getCode())
                            .proficiency(u.getProficiency())
                            .createdAt(u.getCreatedAt())
                            .updatedAt(u.getUpdatedAt())
                            .build()).toList();

            return UserDetailDTO.builder()
                    .name(user.getName())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .mobile(user.getMobile())
                    .profile(user.getProfile())
                    .role(user.getRole())
                    .isActive(user.getIsActive())
                    .isBlocked(user.getIsBlocked())
                    .guid(user.getGuid())
                    .createdAt(user.getCreatedAt() != null ? user.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                    .lastLoginAt(user.getLastLoginAt() != null ? user.getLastLoginAt().toInstant().atZone(ZoneId.systemDefault()) : null)

                    .educations(educationListDTOs)
                    .skills(skillListDTOs)
                    .workExperiences(workExperienceListDTOs)
                    .preferredIndustries(preferredIndustryListDTOs)
                    .userLanguages(userLanguageListDTOs)
                    .build();
        }
        catch (Exception e) {
            throw e;
        }
    }

    public UserProfileDTO userProfile(String email) {
        try {
            User user = findByEmail(email);
            List<Education> educationList = educationRepository.findAllByUserAndIsDeletedFalse(user);
            List<Skill> skillList = skillRepository.findAllByUserAndIsDeletedFalse(user);
            List<WorkExperience> workExperienceList = workExperienceRepository.findAllByUserAndIsDeletedFalse(user);
            List<PreferredIndustry> preferredIndustryList = preferredIndustryRepository.findAllByUserAndIsDeletedFalse(user);
            List<UserLanguage> userLanguageList = userLanguageRepository.findAllByUserAndIsDeletedFalse(user);

            List<EducationListDTO> educationListDTOs = educationList.stream()
                    .map(e -> EducationListDTO.builder()
                            .institution(e.getInstitution())
                            .degree(e.getDegree())
                            .startDate(e.getStartDate())
                            .endDate(e.getEndDate())
                            .guid(e.getGuid())
                            .createdAt(e.getCreatedAt())
                            .updatedAt(e.getUpdatedAt())
                            .build()).toList();

            List<SkillListDTO> skillListDTOs = skillList.stream()
                    .map(s -> SkillListDTO.builder()
                            .name(s.getName())
                            .proficiency(s.getProficiency())
                            .guid(s.getGuid())
                            .createdAt(s.getCreatedAt())
                            .updatedAt(s.getUpdatedAt())
                            .build()).toList();

            List<WorkExperienceListDTO> workExperienceListDTOs = workExperienceList.stream()
                    .map(w -> WorkExperienceListDTO.builder()
                            .companyName(w.getCompanyName())
                            .position(w.getPosition())
                            .startDate(w.getStartDate())
                            .endDate(w.getEndDate())
                            .description(w.getDescription())
                            .guid(w.getGuid())
                            .createdAt(w.getCreatedAt())
                            .updatedAt(w.getUpdatedAt())
                            .build()).toList();

            List<PreferredIndustryListDTO> preferredIndustryListDTOs = preferredIndustryList.stream()
                    .map(p -> PreferredIndustryListDTO.builder()
                            .guid(p.getGuid())
                            .industryName(p.getIndustry().getName())
                            .industryGuid(p.getIndustry().getGuid())
                            .createdAt(p.getCreatedAt())
                            .updatedAt(p.getUpdatedAt())
                            .build()).toList();

            List<UserLanguageListDTO> userLanguageListDTOs = userLanguageList.stream()
                    .map(u -> UserLanguageListDTO.builder()
                            .guid(u.getGuid())
                            .languageName(u.getLanguage().getName())
                            .languageCode(u.getLanguage().getCode())
                            .proficiency(u.getProficiency())
                            .createdAt(u.getCreatedAt())
                            .updatedAt(u.getUpdatedAt())
                            .build()).toList();

            return UserProfileDTO.builder()
                    .name(user.getName())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .mobile(user.getMobile())
                    .profile(user.getProfile())
                    .preferredLanguage(user.getPreferredLanguage())
                    .role(user.getRole())
                    .isActive(user.getIsActive())
                    .isBlocked(user.getIsBlocked())
                    .guid(user.getGuid())
                    .createdAt(user.getCreatedAt() != null ? user.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                    .lastLoginAt(user.getLastLoginAt() != null ? user.getLastLoginAt().toInstant().atZone(ZoneId.systemDefault()) : null)

                    .educations(educationListDTOs)
                    .skills(skillListDTOs)
                    .workExperiences(workExperienceListDTOs)
                    .preferredIndustries(preferredIndustryListDTOs)
                    .userLanguages(userLanguageListDTOs)
                    .build();
        }
        catch (Exception e) {
            throw e;
        }
    }

    public List<CompanyListDTO> getUserCompanies(String email) {
        try {
            User user = findByEmail(email);
            List<CompanyUser> companyUsers = companyUserRepository.findAllByUserAndIsDeletedFalse(user);
            return companyUsers.stream()
                    .map(cu -> {
                        return CompanyListDTO.builder()
                                .name(cu.getCompany().getName())
                                .email(cu.getCompany().getEmail())
                                .logo(cu.getCompany().getLogo())
                                .guid(cu.getCompany().getGuid())
                                .createdAt(cu.getCompany().getCreatedAt())
                                .updatedAt(cu.getCompany().getUpdatedAt())
                                .build();
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw e;
        }
    }
}
