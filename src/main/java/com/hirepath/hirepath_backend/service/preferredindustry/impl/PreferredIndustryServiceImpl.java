package com.hirepath.hirepath_backend.service.preferredindustry.impl;

import com.hirepath.hirepath_backend.model.entity.industry.Industry;
import com.hirepath.hirepath_backend.model.entity.preferredindustry.PreferredIndustry;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.preferredindustry.PreferredIndustryCreateRequest;
import com.hirepath.hirepath_backend.repository.preferredindustry.PreferredIndustryRepository;
import com.hirepath.hirepath_backend.service.industry.IndustryService;
import com.hirepath.hirepath_backend.service.preferredindustry.PreferredIndustryService;
import com.hirepath.hirepath_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PreferredIndustryServiceImpl implements PreferredIndustryService {

    private final PreferredIndustryRepository preferredIndustryRepository;
    private final UserService userService;
    private final IndustryService industryService;

    @Override
    public PreferredIndustry findByGuid(String guid) {
        try {
            return preferredIndustryRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PreferredIndustry not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void preferredIndustryCreate(PreferredIndustryCreateRequest request, String email) {
        try {
            User user = userService.findByEmail(email);
            Industry industry = industryService.findByGuid(request.getIndustryGuid());

            PreferredIndustry preferredIndustry = PreferredIndustry.builder()
                    .user(user)
                    .industry(industry)
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(user.getId())
                    .build();

            preferredIndustryRepository.save(preferredIndustry);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void preferredIndustryDelete(String preferredIndustryGuid, String email) {
        try {
            User user = userService.findByEmail(email);
            PreferredIndustry preferredIndustry = findByGuid(preferredIndustryGuid);
            if (!user.equals(preferredIndustry.getUser())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this item");
            }

            preferredIndustry.setIsDeleted(true);
            preferredIndustry.setUpdatedAt(ZonedDateTime.now());
            preferredIndustry.setUpdatedBy(user.getId());

            preferredIndustryRepository.save(preferredIndustry);
        } catch (Exception e) {
            throw e;
        }
    }
}
