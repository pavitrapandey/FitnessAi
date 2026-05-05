package com.fitnessAi.Gateway.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static reactor.netty.http.HttpConnectionLiveness.log;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {



   private Logger logger= LoggerFactory.getLogger(UserService.class);
    private final WebClient webClient; // inject LoadBalanced builder if using service discovery

    public Mono<Boolean> validateUser(String userId) {

            // direct host:port to avoid unresolved hostname from discovery

           try {
               return webClient.get()
                       .uri("/api/user/{user_id}/validate", userId)
                       .retrieve()
                       .bodyToMono(Boolean.class)
                       .onErrorResume(WebClientResponseException.class, e -> {

                                   if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                                       return Mono.error(new RuntimeException("User not found"));

                                   else if (e.getStatusCode() == HttpStatus.BAD_REQUEST)
                                       return Mono.error(new RuntimeException("Invalid Request"));

                                   return Mono.error(new RuntimeException("Server Error"));
                               }
                       );
           } catch (Exception e) {
               logger.error("Error validating user", e);
               return Mono.error(new RuntimeException("Error validating user"));
           }
    }


    public Mono<UserResponse> registerUser(RegisterRequest registerRequest) {
        log.info("Calling Registering user: {}", registerRequest.getEmail());
        return webClient.post()
                .uri("/api/user/register")
                .bodyValue(registerRequest)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .onErrorResume(WebClientResponseException.class, e -> {

                           if (e.getStatusCode() == HttpStatus.BAD_REQUEST)
                                return Mono.error(new RuntimeException("Invalid Request "+e.getMessage()));

                            return Mono.error(new RuntimeException("Server Error "+e.getMessage()));
                        }
                );
    }
}
