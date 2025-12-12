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

import java.util.UUID;

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

        boolean isValid=userValidationService.validateUser(request.getUserId());
        if(!isValid){
            throw new UserNotFoundException("User not found");
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


}
