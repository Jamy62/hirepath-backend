package com.hirepath.hirepath_backend.model.request.application;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

import java.util.List;

@AutoNotBlank
@Data
public class ApplicationAcceptRequest {
    private List<String> positionGuids;
}
