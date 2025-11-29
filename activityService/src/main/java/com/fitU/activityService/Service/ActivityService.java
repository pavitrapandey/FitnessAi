package com.fitU.activityService.Service;

import com.fitU.activityService.Dto.ActivityRequest;
import com.fitU.activityService.Dto.ActivityResponse;
import com.fitU.activityService.Exception.UserNotFoundException;
import com.fitU.activityService.Model.Activity;
import com.fitU.activityService.Repository.ActivityRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivityService {
    @Autowired
    private ActivityRepo activityRepo;

    private UserValidationService userValidationService;

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
        activityRepo.save(activity);
        return mapper.map(activity,ActivityResponse.class);
    }


}
