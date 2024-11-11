package com.dev.hlvsbackend.controllers;

import com.dev.hlvsbackend.domain.dtos.GeneralResponse;
import com.dev.hlvsbackend.domain.dtos.House.GetHouseDTO;
import com.dev.hlvsbackend.domain.dtos.House.RegisterHouseDTO;
import com.dev.hlvsbackend.domain.dtos.House.RegisterResidentsDTO;
import com.dev.hlvsbackend.domain.dtos.User.GetUserDTO;
import com.dev.hlvsbackend.domain.entities.House;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.domain.enums.UserTypeE;
import com.dev.hlvsbackend.repositories.UserRepository;
import com.dev.hlvsbackend.services.HouseService;
import com.dev.hlvsbackend.services.PermissionService;
import com.dev.hlvsbackend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/residential/house")
public class HouseController {

    private final HouseService houseService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PermissionService permissionService;

    public HouseController(
            HouseService houseService,
            UserRepository userRepository,
            UserService userService,
            PermissionService permissionService
    ) {
        this.houseService = houseService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.permissionService = permissionService;
    }

    //@GetMapping("/all")

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getHouseById(@PathVariable("id") String id) {
        try {
            House house = houseService.getHouseByHouseNumber(id);

            if (house == null) {
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_ACCEPTABLE,
                        "This house does not exist!"
                );
            }

            GetHouseDTO response = new GetHouseDTO();

            response.setHouse_number(house.getNumber());
            response.setDireccion(house.getDireccion());
            response.setCantidad_residentes(house.getCantidad_residentes());

            List<User> userlist = house.getUsers();
            if (userlist.isEmpty()){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "This house does not contain guests!"
                );
            }

            List<GetUserDTO> list = new ArrayList<>();
            userlist.forEach( userf -> {
                GetUserDTO dto = new GetUserDTO();
                dto.setId(userf.getId().toString());
                dto.setNombre(userf.getNombre());
                dto.setCorreo_google(userf.getCorreo());
                dto.setId_casa(userf.getCasa().getId().intValue());
                dto.setTipo_usuario(userf.getUserType().toString());

                list.add(dto);
            });

            response.setUsers(list);

            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    response
            );

        } catch (Exception e) {
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when getting house: " + id,
                    e.getMessage()
            );
        }

    }

    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> registerHouse(@RequestBody @Valid RegisterHouseDTO data, BindingResult error) {
        if (error.hasErrors()) {
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating user",
                    error.getAllErrors()
            );
        }
        try {
            House house = houseService.getHouseByHouseNumber(data.getId());

            if (house != null) {
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_ACCEPTABLE,
                        "This house is already registered!",
                        data
                );
            }

            User charged = userRepository.findUserByCorreo(data.getUsers()).orElse(null);

            if (charged == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_ACCEPTABLE,
                        "This user doesn't exists!",
                        data
                );
            }

            houseService.createHouseService(data);
            House response = userService.setHouseToUser(charged, data.getId(), UserTypeE.SUPERVISOR);
            permissionService.CreatePermissionSupervisor(charged, response);

            return GeneralResponse.getResponse(
                    HttpStatus.CREATED,
                    response
            );
        } catch (Exception e) {
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating house",
                    e.getMessage()
            );
        }
    }

    @PostMapping("/register-residents")
    public ResponseEntity<GeneralResponse> RegisterResidents(@RequestBody @Valid RegisterResidentsDTO data) {
        try {
            House house = houseService.getHouseByHouseNumber(data.getHouse_id());
            if (house == null) {
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_ACCEPTABLE,
                        "This house does not exist!"
                );
            }

            if (house.getUsers().size() >= house.getCantidad_residentes()){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_ACCEPTABLE,
                        "This house has the maximum of guests!"
                );
            }

            User user = userService.getUserByEmail(data.getEmail());
            if (user == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "User not found: " + data.getEmail()
                );
            }

            if (user.getCasa() != null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_ACCEPTABLE,
                        "This user already has a house: " + user.getCasa().getId()
                );
            }

            userService.setHouseToUser(user, data.getHouse_id(), UserTypeE.USER);
            permissionService.CreatePermissionGuest(user, house);

            return GeneralResponse.getResponse(
                    HttpStatus.OK
            );

        } catch (Exception e) {
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when getting house",
                    e.getMessage()
            );
        }

    }

    @GetMapping("/user-house/{email}")
    public ResponseEntity<GeneralResponse> FindUserHouse(@PathVariable("email") String email) {
        try {
            User user = userService.getUserByEmail(email);
            if (user == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "User not found!"
                );
            }

            House house = houseService.getHouseByUser(user);

            if (house == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "House not found!"
                );
            }

            GetHouseDTO response = new GetHouseDTO();

            response.setHouse_number(house.getNumber());
            response.setDireccion(house.getDireccion());
            response.setCantidad_residentes(house.getCantidad_residentes());

            List<User> userlist = userRepository.findByCasa(house).orElse(null);

            if (userlist.isEmpty()){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "This house does not contain guests!"
                );
            }

            List<GetUserDTO> list = new ArrayList<>();

            userlist.forEach( userf -> {
                GetUserDTO dto = new GetUserDTO();
                dto.setId(userf.getId().toString());
                dto.setNombre(userf.getNombre());
                dto.setCorreo_google(userf.getCorreo());
                dto.setId_casa(userf.getCasa().getId().intValue());
                dto.setTipo_usuario(userf.getUserType().toString());

                list.add(dto);
            });

            response.setUsers(list);

            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    response
            );

        }catch (Exception e){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when getting data",
                    e.getMessage()
            );
        }

    }

}
