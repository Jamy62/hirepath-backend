package com.hirepath.hirepath_backend.service.plan.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.plan.PlanListDTO;
import com.hirepath.hirepath_backend.model.dto.plan.PlanListProjection;
import com.hirepath.hirepath_backend.model.entity.plan.Plan;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.plan.PlanCreateRequest;
import com.hirepath.hirepath_backend.model.request.plan.PlanUpdateRequest;
import com.hirepath.hirepath_backend.repository.plan.PlanRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.plan.PlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    @Override
    public void planCreate(PlanCreateRequest request, String adminEmail) {
        try {
            User admin = userRepository.findByEmail(adminEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

            Plan plan = Plan.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .price(request.getPrice())
                    .duration(request.getDuration())
                    .durationDays(request.getDurationDays())
                    .features(request.getFeatures())
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(admin.getId())
                    .build();

            planRepository.save(plan);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<PlanListDTO> planList(String searchName, String orderBy, int first, int max) {
        try {
            if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
                List<PlanListProjection> planListProjection = planRepository.findAllPlansAdminPanel(searchName, orderBy, first, max);

                return planListProjection.stream()
                        .map(p -> PlanListDTO.builder()
                                .name(p.getName())
                                .description(p.getDescription())
                                .price(p.getPrice())
                                .duration(p.getDuration())
                                .durationDays(p.getDurationDays())
                                .features(p.getFeatures())
                                .guid(p.getGuid())
                                .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .updatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .build())
                        .toList();
            }

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Please enter either ASC or DESC for orderBy");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void planUpdate(String planGuid, PlanUpdateRequest request, String email) {
        try {
            User admin = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

            Plan plan = planRepository.findByGuid(planGuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Plan not found"));

            if (request.getName() != null && !request.getName().isBlank()) {
                plan.setName(request.getName());
            }
            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                plan.setDescription(request.getDescription());
            }
            if (request.getPrice() != null && request.getPrice().signum() >= 0) {
                plan.setPrice(request.getPrice());
            }
            if (request.getDuration() != null && !request.getDuration().isBlank()) {
                plan.setDuration(request.getDuration());
            }
            if (request.getDurationDays() != null && request.getDurationDays() > 0) {
                plan.setDurationDays(request.getDurationDays());
            }
            if (request.getFeatures() != null && !request.getFeatures().isBlank()) {
                plan.setFeatures(request.getFeatures());
            }

            plan.setUpdatedAt(ZonedDateTime.now());
            plan.setUpdatedBy(admin.getId());

            planRepository.save(plan);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void planDelete(String planGuid, String adminEmail) {
        try {
            User admin = userRepository.findByEmail(adminEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

            Plan plan = planRepository.findByGuid(planGuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Plan not found"));

            plan.setIsDeleted(true);
            plan.setUpdatedAt(ZonedDateTime.now());
            plan.setUpdatedBy(admin.getId());

            planRepository.save(plan);
        } catch (Exception e) {
            throw e;
        }
    }
}
