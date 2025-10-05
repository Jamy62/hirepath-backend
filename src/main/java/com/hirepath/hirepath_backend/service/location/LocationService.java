package com.hirepath.hirepath_backend.service.location;

import com.hirepath.hirepath_backend.model.entity.location.Location;

public interface LocationService {
    Location findByGuid(String guid);
}
