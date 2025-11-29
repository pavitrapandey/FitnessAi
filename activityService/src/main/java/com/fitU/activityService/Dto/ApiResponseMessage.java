package com.fitU.activityService.Dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Builder
@Data
public class ApiResponseMessage {
    private String message;
    private boolean success;
    private HttpStatus status;
}
