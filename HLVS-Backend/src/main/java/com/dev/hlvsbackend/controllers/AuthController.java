package com.dev.hlvsbackend.controllers;

import com.dev.hlvsbackend.domain.dtos.Auth.TokenDTO;
import com.dev.hlvsbackend.domain.dtos.GeneralResponse;
import com.dev.hlvsbackend.domain.entities.Token;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.repositories.UserRepository;
import com.dev.hlvsbackend.services.AuthService;
import com.dev.hlvsbackend.services.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final UserService userService;

    public AuthController(
            AuthService authService,
            UserRepository userRepository,
            UserService userService
    ){
        this.authService = authService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/login/{token}")
    public ResponseEntity<GeneralResponse> login(@PathVariable String token) {
        try{
            String data = authService.VerifyGoogle(token);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode dataJson = mapper.readTree(data);
            User user = userRepository.findUserByCorreo(dataJson.path("email").asText()).orElse(null);

            if (user != null){
                Token newtoken = userService.registerToken(user);
                TokenDTO response = new TokenDTO();
                response.setToken(newtoken.getContent());
                return GeneralResponse.getResponse(
                        HttpStatus.ACCEPTED,
                        "Login successful",
                        response
                );
            }

            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    "Redirecting to register user form",
                    data
            );

        }catch (Exception e){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error fetching data",
                    e.getMessage()
            );
        }
    }

    @PostMapping("/logout/{identifier}")
    public ResponseEntity<GeneralResponse> logout(@PathVariable String identifier) throws Exception {
        try {
            User user = userRepository.findUserByCorreo(identifier).orElse(null);
            if (user == null) {
                return GeneralResponse.getResponse(
                        HttpStatus.CONFLICT,
                        "User doesn't exists"
                );
            }

            userService.cleanTokens(user);

            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    "Logout successful"
            );
        }catch (Exception e){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error fetching data",
                    e.getMessage()
            );
        }
    }
}
