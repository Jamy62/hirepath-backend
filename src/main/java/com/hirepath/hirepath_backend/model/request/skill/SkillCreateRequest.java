package com.hirepath.hirepath_backend.model.request.skill;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

@Data
@AutoNotBlank
public class SkillCreateRequest {
    private String name;
    private String proficiency;
}
