package com.fitU.activityService.Service;

import com.fitU.activityService.Configuration.WebClientConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Service
@RequiredArgsConstructor
public class UserValidationService {



   private Logger logger= LoggerFactory.getLogger(UserValidationService.class);
    private final WebClient webClient; // inject LoadBalanced builder if using service discovery

    public boolean validateUser(String userId) {

            // direct host:port to avoid unresolved hostname from discovery

            Boolean isValid =webClient.get()
                    .uri("/api/user/{user_id}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            logger.info(String.valueOf(webClient.get()
                    .uri("/api/user/{user_id}/validate", userId)));
            return Boolean.TRUE.equals(isValid);
    }
}
