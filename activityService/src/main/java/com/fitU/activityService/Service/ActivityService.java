package com.fitU.activityService.Service;

import com.fitU.activityService.Dto.ActivityRequest;
import com.fitU.activityService.Dto.ActivityResponse;
import com.fitU.activityService.Exception.UserNotFoundException;
import com.fitU.activityService.Model.Activity;
import com.fitU.activityService.Repository.ActivityRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {
    @Autowired
    private ActivityRepo activityRepo;

    @Autowired
    private UserValidationService userValidationService;

    private final KafkaTemplate<String, Activity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    @Autowired
    private ModelMapper mapper;

    public ActivityResponse trackActivity(ActivityRequest request) {

        // Skip user validation if userId is null or empty
        if(request.getUserId() != null && !request.getUserId().isEmpty()) {
            boolean isValid=userValidationService.validateUser(request.getUserId());
            if(!isValid){
                throw new UserNotFoundException("User not found");
            }
        }

        Activity activity= Activity.builder()
                .id(UUID.randomUUID().toString())
                .userId(request.getUserId())
                .type(request.getType())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .notes(request.getNotes())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();
       Activity savedActivity= activityRepo.save(activity);
       try {
           kafkaTemplate.send(topicName, savedActivity.getUserId(), savedActivity);
       } catch (Exception e) {
           e.printStackTrace();
       }
        return mapper.map(savedActivity,ActivityResponse.class);
    }

    public List<ActivityResponse> getAllActivitiesByUserId(String userId) {
        List<Activity> activities;
        if(userId == null || userId.isEmpty()) {
            // Return all activities if no userId specified
            activities = activityRepo.findAll();
        } else {
            activities = activityRepo.findByUserId(userId);
        }
        return activities.stream()
                .map(activity -> mapper.map(activity, ActivityResponse.class))
                .collect(Collectors.toList());
    }

    public List<ActivityResponse> getAllActivities() {
        List<Activity> activities = activityRepo.findAll();
        return activities.stream()
                .map(activity -> mapper.map(activity, ActivityResponse.class))
                .collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(String activityId) {
        Activity activity = activityRepo.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: " + activityId));
        return mapper.map(activity, ActivityResponse.class);
    }

    public void deleteActivity(String activityId) {
        Activity activity = activityRepo.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: " + activityId));
        activityRepo.delete(activity);
    }

}
