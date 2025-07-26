package com.hirepath.hirepath_backend.util;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

public class SecurityUtil {
    public void checkValidCompany(String companyGuid, String... allowedRoles) {
        Map<String, Object> details = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getDetails();

        if (details == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No companies available");
        }
        Map<String, String> companyRoles = (Map<String, String>) details.get("companyRoles");

        if (companyRoles == null || !companyRoles.containsKey(companyGuid) || !Arrays.asList(allowedRoles).contains(companyRoles.get(companyGuid))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this company");
        }
    }
}
