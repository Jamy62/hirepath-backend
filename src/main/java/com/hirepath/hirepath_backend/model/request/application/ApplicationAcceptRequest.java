package com.hirepath.hirepath_backend.model.request.application;

import lombok.Data;

import java.util.List;

@Data
public class ApplicationAcceptRequest {
    private List<String> positionGuids;
}
