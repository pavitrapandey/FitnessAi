package com.fitU.activityService.Controller;

import com.fitU.activityService.Dto.ActivityRequest;
import com.fitU.activityService.Dto.ActivityResponse;
import com.fitU.activityService.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
public class ActivityController{

    @Autowired
    private ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request){
        return new ResponseEntity<>(activityService.trackActivity(request), HttpStatus.CREATED);
    }
}
