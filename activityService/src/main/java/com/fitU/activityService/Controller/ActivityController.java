package com.fitU.activityService.Controller;

import com.fitU.activityService.Dto.ActivityRequest;
import com.fitU.activityService.Dto.ActivityResponse;
import com.fitU.activityService.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController{

    @Autowired
    private ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request){
        return new ResponseEntity<>(activityService.trackActivity(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getActivitiesByUserId(
            @RequestHeader(value = "X-USER-ID", required = false) String userId){
        // Return all activities regardless of userId for now (development mode)
        return new ResponseEntity<>(activityService.getAllActivities(), HttpStatus.OK);
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getActivityById(@PathVariable String activityId){
        return new ResponseEntity<>(activityService.getActivityById(activityId), HttpStatus.OK);
    }

    @DeleteMapping("/{activityId}")
    public ResponseEntity<Void> deleteActivity(@PathVariable String activityId){
        activityService.deleteActivity(activityId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
