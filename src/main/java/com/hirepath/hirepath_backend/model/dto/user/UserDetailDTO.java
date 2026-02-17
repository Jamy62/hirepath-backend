package com.hirepath.hirepath_backend.model.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hirepath.hirepath_backend.model.dto.education.EducationListDTO;
import com.hirepath.hirepath_backend.model.dto.preferredindustry.PreferredIndustryListDTO;
import com.hirepath.hirepath_backend.model.dto.skill.SkillListDTO;
import com.hirepath.hirepath_backend.model.dto.userlanguage.UserLanguageListDTO;
import com.hirepath.hirepath_backend.model.dto.workexperience.WorkExperienceListDTO;
import com.hirepath.hirepath_backend.model.entity.preferredlanguage.PreferredLanguage;
import com.hirepath.hirepath_backend.model.entity.role.Role;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDetailDTO {
    private String name;
    private String fullName;
    private String email;
    private String mobile;
    private String profile;
    private Role role;
    private PreferredLanguage preferredLanguage;
    private Boolean isActive;
    private Boolean isBlocked;
    private String guid;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime createdAt;
    private ZonedDateTime lastLoginAt;

    private List<EducationListDTO> educations;
    private List<SkillListDTO> skills;
    private List<WorkExperienceListDTO> workExperiences;
    private List<PreferredIndustryListDTO> preferredIndustries;
    private List<UserLanguageListDTO> userLanguages;
}
