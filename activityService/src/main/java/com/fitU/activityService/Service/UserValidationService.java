package com.fitU.activityService.Service;

import com.fitU.activityService.Configuration.WebClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Service
@RequiredArgsConstructor
public class UserValidationService {


    private final WebClient userWebClient;

    public boolean validateUser(String userId) {

        try {
            return Boolean.TRUE.equals(userWebClient.get()
                    .uri("/api/user/{user_id}/validate" + userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block());
        } catch (WebClientException e) {
            e.printStackTrace();
        }

        return false;
    }
}
