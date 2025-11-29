package com.fitU.activityService.Dto;

import com.fitU.activityService.Model.ActivityType;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityRequest {
    private String userId;
    private ActivityType type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer duration;
    private Integer caloriesBurned;
    private String notes;
    private Map<String,Object> additionalMetrics;
}
