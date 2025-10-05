package com.hirepath.hirepath_backend.util;

import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

public class AuthenticationUtil {

    public static void isPageMember(Object authDetail, String companyGuid) {
        String authCompanyGuid = ((Map<String, String>) authDetail).get("companyGuid");

        if (authCompanyGuid == null || !authCompanyGuid.equals(companyGuid)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not belong to this company.");
        }
    }

    public static String getGuid(Object authDetail) {
        return ((Map<String, String>) authDetail).get("companyGuid");
    }
}
