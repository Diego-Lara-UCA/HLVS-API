package com.dev.hlvsbackend.controllers;

import com.dev.hlvsbackend.domain.dtos.Auth.TokenDTO;
import com.dev.hlvsbackend.domain.dtos.GeneralResponse;
import com.dev.hlvsbackend.domain.entities.Token;
import com.dev.hlvsbackend.services.AuthService;
import com.dev.hlvsbackend.utils.UserUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(
            AuthService authService
    ){
        this.authService = authService;
    }

    @PostMapping("/login/{token}")
    public ResponseEntity<GeneralResponse> login(@PathVariable String token) {
        try{
            Token data = authService.VerifyGoogle(token);
            TokenDTO response = new TokenDTO();
            response.setToken(data.getContent());
            return GeneralResponse.getResponse(
                    HttpStatus.ACCEPTED,
                    "Login successful",
                    response
            );
        }

        catch (UserUtils.UserNotFoundException e) {
            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    "Redirecting to register user form"
            );
        }
        catch (Exception e){
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
            authService.logout(identifier);
            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    "Logout successful"
            );
        }
        catch (UserUtils.UserNotFoundException e) {
            return GeneralResponse.getResponse(
                    HttpStatus.NOT_FOUND,
                    "User doesn't exists"
            );
        }
        catch (Exception e){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error fetching data",
                    e.getMessage()
            );
        }
    }
}
