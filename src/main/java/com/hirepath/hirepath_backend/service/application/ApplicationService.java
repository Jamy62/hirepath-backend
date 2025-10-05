package com.hirepath.hirepath_backend.service.application;

import com.hirepath.hirepath_backend.model.entity.application.Application;

public interface ApplicationService {
    Application findByGuid(String guid);
}
