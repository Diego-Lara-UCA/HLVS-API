package com.dev.hlvsbackend.controllers;

import com.dev.hlvsbackend.domain.dtos.GeneralResponse;
import com.dev.hlvsbackend.domain.dtos.User.GetUserDTO;
import com.dev.hlvsbackend.domain.dtos.User.RegisterGuardDTO;
import com.dev.hlvsbackend.domain.dtos.User.RegisterUserDTO;
import com.dev.hlvsbackend.domain.dtos.User.VerifyUserDTO;
import com.dev.hlvsbackend.domain.entities.House;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.domain.enums.UserTypeE;
import com.dev.hlvsbackend.services.UserService;
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
    public UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<GeneralResponse> findAll() {
        return GeneralResponse.getResponse(
                HttpStatus.OK,
                "List of users!",
                userService.getAllUsers());
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

            GetUserDTO response = new GetUserDTO();
            response.setId(user.getId().toString());
            response.setNombre(user.getNombre());
            response.setCorreo_google(user.getCorreo());
            response.setTipo_documento(user.getTipo_documento().toString());
            response.setNumero_documento(user.getNumero_documento());

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

            List<GetUserDTO> response = new ArrayList<>();

            list.forEach(guard -> {
                GetUserDTO dto = new GetUserDTO();
                dto.setId(guard.getId().toString());
                dto.setNombre(guard.getNombre());
                dto.setCorreo_google(guard.getCorreo());
                dto.setTipo_usuario(guard.getUserType().toString());
                dto.setTipo_documento(guard.getTipo_documento().toString());
                dto.setNumero_documento(guard.getNumero_documento());
                response.add(dto);
            });

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
