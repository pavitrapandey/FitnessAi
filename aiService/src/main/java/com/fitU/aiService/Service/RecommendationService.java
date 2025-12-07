package com.fitU.aiService.Service;

import com.fitU.aiService.Model.Recommendation;
import com.fitU.aiService.Repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    public List<Recommendation> getRecommendationsByUserId(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    public Recommendation getRecommendationByActivity(String activityId){
        return recommendationRepository.findByActivityId(activityId).orElseThrow(
                () -> new RuntimeException("Recommendation not found for activityId: " + activityId)
        );
    }
}
