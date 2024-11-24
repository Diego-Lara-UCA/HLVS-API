package com.dev.hlvsbackend.services.implementations;

import com.dev.hlvsbackend.domain.entities.Token;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.services.AuthService;
import com.dev.hlvsbackend.services.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class AuthImplementation implements AuthService{

    private final WebClient webClient;
    private final UserService userService;

    @Autowired
    public AuthImplementation (
            WebClient.Builder webClient,
            UserService userRepository
    ){
        this.userService = userRepository;
        this.webClient = webClient.build();
    }

    @Override
    public String VerifyGoogle(String token){
        System.out.println(token);

        String responseBody = webClient.post()
                .uri("https://oauth2.googleapis.com/tokeninfo?access_token="+token)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("ok");

        return responseBody;
    }

    @Override
    public void logout(User user){
        try {
            userService.cleanTokens(user);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
