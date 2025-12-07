package com.fitU.aiService.Controller;

import com.fitU.aiService.Model.Recommendation;
import com.fitU.aiService.Service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getRecommendationsByUserId(@PathVariable String userId) {
        return new ResponseEntity<>(recommendationService.getRecommendationsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommendation> getRecommendationByActivity(@PathVariable String activityId) {
        return new ResponseEntity<>(recommendationService.getRecommendationByActivity(activityId), HttpStatus.OK);
    }
}
