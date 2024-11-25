package com.dev.hlvsbackend.controllers;

import com.dev.hlvsbackend.domain.dtos.Auth.TokenDTO;
import com.dev.hlvsbackend.domain.dtos.GeneralResponse;
import com.dev.hlvsbackend.domain.dtos.User.GetUserDTO;
import com.dev.hlvsbackend.domain.dtos.User.RegisterGuardDTO;
import com.dev.hlvsbackend.domain.dtos.User.RegisterUserDTO;
import com.dev.hlvsbackend.domain.dtos.User.VerifyUserDTO;
import com.dev.hlvsbackend.domain.entities.House;
import com.dev.hlvsbackend.domain.entities.Token;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.domain.enums.UserTypeE;
import com.dev.hlvsbackend.services.UserService;
import com.dev.hlvsbackend.utils.UserUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserUtils userUtils;

    public UserController(
            UserService userService,
            UserUtils userUtils
    ){
        this.userService = userService;
        this.userUtils = userUtils;
    }

    @GetMapping("/all")
    public ResponseEntity<GeneralResponse> findAll() {
        List<GetUserDTO> response = userUtils.CreateListOfGetUserDTO(userService.getAllUsers());
        return GeneralResponse.getResponse(
                HttpStatus.OK,
                "List of users!",
                response
        );
    }

    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> RegisterUser(@RequestBody @Valid RegisterUserDTO data, BindingResult error){
        if (error.hasErrors()){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating user",
                    error.getAllErrors()
            );
        }
        try{
            User user = userService.getUserByEmail(data.getEmail());
            if (user != null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_ACCEPTABLE,
                        "This email is already registered!",
                        data
                );
            }

            String message = userService.registerUser(data);

            if (message == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_ACCEPTABLE,
                        "Error when creating user, verify body data",
                        data
                );
            }

            /*Token token = userService.registerToken(user);*/
            return GeneralResponse.getResponse(
                HttpStatus.CREATED,
                message
                /*new TokenDTO(token.getContent())*/
            );
        }catch (Exception e){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating user",
                    e.getMessage()
            );
        }
    }

    //@GetMapping("/all")
    @GetMapping("/get-user")
    public ResponseEntity<GeneralResponse> VerifyUser(@RequestBody @Valid VerifyUserDTO data, BindingResult error){
        if (error.hasErrors()){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating user",
                    error.getAllErrors()
            );
        }
        try{
            User user = userService.getUserByEmail(data.getEmail());
            if (user == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "User not found!"
                );
            }

            GetUserDTO response = userUtils.CreateGetUserDTO(user);
            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    "User found!",
                    response
            );

        }catch (Exception e){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating user",
                    e.getMessage()
            );
        }
    }

    @PostMapping("register-guard")
    public ResponseEntity<GeneralResponse> RegisterGuard(@RequestBody @Valid RegisterGuardDTO data, BindingResult error){
        if (error.hasErrors()){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating user",
                    error.getAllErrors()
            );
        }
        try{
            User user = userService.getUserByEmail(data.getEmail());
            if (user == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "User not found",
                        data
                );
            }

            if (user.getUserType() != UserTypeE.GUEST){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_ACCEPTABLE,
                        "User must be GUEST type",
                        data
                );
            }

            String message = userService.registerGuard(user);
            if (message == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_ACCEPTABLE,
                        "Error when creating guard, verify body data",
                        data
                );
            }

            return GeneralResponse.getResponse(
                    HttpStatus.CREATED,
                    message
            );
        }catch (Exception e){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating user",
                    e.getMessage()
            );
        }
    }

    @GetMapping("/all-guards")
    public ResponseEntity<GeneralResponse> findAllGuards() {
        try{
            List<User> list = userService.getAllGuards();
            if (list.isEmpty()){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "No guards registered!"
                );
            }

            List<GetUserDTO> response = userUtils.CreateListOfGetUserDTO(list);

            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    "List of guards",
                    response
            );
        }catch (Exception e){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating user",
                    e.getMessage()
            );
        }
    }
}
