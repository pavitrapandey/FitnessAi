package com.fitU.activityService.Model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collation = "activity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Activity {

    @Id

    private String id;
    private String userId;
    private ActivityType type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer duration;
    private Integer caloriesBurned;
    private String notes;

    @Field("metrics")
    private Map<String,Object> additionalMetrics;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
