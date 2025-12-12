package com.fitU.aiService.Service;

import com.fitU.aiService.Model.Activity;
import com.fitU.aiService.Model.Recommendation;
import com.fitU.aiService.Repository.RecommendationRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityListenerService {

    private final ActivityAiService activityAiService;
    private final RecommendationRepository recommendationRepository;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "activity-processor-group")
    public void processActivity(Activity activity) {
        log.info("Processing activity: {}", activity.getUserId());
       Recommendation recommendation =  activityAiService.generateRecommendation(activity);
       recommendationRepository.save(recommendation);
    }
}
