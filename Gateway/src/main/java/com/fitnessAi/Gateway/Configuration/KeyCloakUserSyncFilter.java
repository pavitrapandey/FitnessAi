package com.fitnessAi.Gateway.Configuration;

import com.fitnessAi.Gateway.User.RegisterRequest;
import com.fitnessAi.Gateway.User.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.text.ParseException;

// @Component - Disabled for now to allow requests without authentication
@Slf4j
@RequiredArgsConstructor
public class KeyCloakUserSyncFilter implements WebFilter {

    private final UserService userService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain)
    {
        String userId = exchange.getRequest().getQueryParams().getFirst("X-USER-ID");
        String token=exchange.getRequest().getHeaders().getFirst("Authorization");
        
        // If no token, just continue the chain (let security handle it)
        if(token == null) {
            return chain.filter(exchange);
        }
        
        RegisterRequest registerRequest=getUserDetails(token);
        if(userId==null && registerRequest != null){
            userId= registerRequest.getKeycloakId();
        }

        if(userId!=null && token!=null) {

            String finalUserId = userId;
            return userService.validateUser(userId)
                    .flatMap(exist ->{
                        if(!exist){
                            if(registerRequest!=null){
                                return userService.registerUser(registerRequest)
                                        .then(Mono.empty());
                            }
                            else{
                                return Mono.empty();
                            }
                        }
                        else{
                            log.info("User already exists, Skipping SYNC");
                            return Mono.empty();
                        }
                    }).then(Mono.defer(()->{
                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-USER-ID", finalUserId)
                                .build();
                        return chain.filter(exchange.mutate().request(mutatedRequest).build());

                    }));
        }
        // Continue the filter chain even if no token/userId
        return chain.filter(exchange);
    }

    private RegisterRequest getUserDetails(String token)
    {
        try {
            String tokenWithoutBearer = token.replace("Bearer ", "").trim();
            SignedJWT jwt = SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claims = jwt.getJWTClaimsSet();

            RegisterRequest request=new RegisterRequest();
            request.setEmail(claims.getStringClaim("email"));
            request.setKeycloakId(claims.getStringClaim("sub"));
            request.setPassword("password");
            request.setFirstName(claims.getStringClaim("given_name"));
            request.setLastName(claims.getStringClaim("family_name"));
            return request;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
