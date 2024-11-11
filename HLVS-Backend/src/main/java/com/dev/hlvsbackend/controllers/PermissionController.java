package com.dev.hlvsbackend.controllers;

import com.dev.hlvsbackend.domain.dtos.EntranceKey.RegisterEntranceKeyDTO;
import com.dev.hlvsbackend.domain.dtos.GeneralResponse;
import com.dev.hlvsbackend.domain.dtos.House.RegisterResidentsDTO;
import com.dev.hlvsbackend.domain.dtos.Permission.PermissionResponseDTO;
import com.dev.hlvsbackend.domain.dtos.Permission.RegisterResidentPermissionDTO;
import com.dev.hlvsbackend.domain.dtos.Permission.RequestPermissionDTO;
import com.dev.hlvsbackend.domain.entities.House;
import com.dev.hlvsbackend.domain.entities.Permission;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.domain.enums.UserTypeE;
import com.dev.hlvsbackend.repositories.HouseRepository;
import com.dev.hlvsbackend.repositories.PermissionRepository;
import com.dev.hlvsbackend.services.PermissionService;
import com.dev.hlvsbackend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/residential/permission")
public class PermissionController {
    private final UserService userService;
    private final PermissionService permissionService;
    private final PermissionRepository permissionRepository;

    private final HouseRepository houseRepository;

    public PermissionController(
            UserService userService,
            PermissionService permissionService,
            PermissionRepository permissionRepository,
            HouseRepository houseRepository
    ){
        this.userService = userService;
        this.permissionService = permissionService;
        this.permissionRepository = permissionRepository;
        this.houseRepository = houseRepository;
    }

    @PostMapping("/request")
    public ResponseEntity<GeneralResponse> RequestPermission(@RequestBody @Valid RequestPermissionDTO data, BindingResult error){
        if (error.hasErrors()){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating user",
                    error.getAllErrors()
            );
        }
        try {
            User user_visit = userService.getUserByEmail(data.getEmail_permission());
            User user_house = userService.getUserByEmail(data.getEmail_house());

            if (user_visit == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "User not found: " + data.getEmail_permission()
                );
            }

            if (user_house == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "User not found: " + data.getEmail_house()
                );
            }

            permissionService.RequestPermission(user_visit, user_house, data);
            return GeneralResponse.getResponse(
                    HttpStatus.CREATED
            );

        }catch (Exception e){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when getting permission",
                    e.getMessage()
            );
        }
    }

    @GetMapping("/permission-details/{email}")
    public ResponseEntity<GeneralResponse> CreatePermission(@PathVariable String email){
        try{
            User user = userService.getUserByEmail(email);
            if (user == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "User not found!"
                );
            }

            List<Permission> list = permissionService.GetPermissionsApprovedByUser(user);
            List<PermissionResponseDTO> response = new ArrayList<>();

            if (list.isEmpty()){
                list.add(null);
                return GeneralResponse.getResponse(
                        HttpStatus.NO_CONTENT,
                        "This user does not has permissions!",
                        list
                );
            }

            list.forEach(permission -> {
                PermissionResponseDTO dto = new PermissionResponseDTO();

                dto.setId(permission.getId());
                dto.setUser(permission.getUser().getCorreo());
                dto.setHouse(permission.getHouse().getNumber());
                dto.setAprovado(permission.getAprovado());
                dto.setActivo(permission.getActivo());
                dto.setDias_semana(permission.getDias_semana());
                dto.setFecha_inicio(permission.getFecha_inicio());
                dto.setFecha_final(permission.getFecha_final());
                dto.setHora_inicio(permission.getHora_inicio());
                dto.setHora_fin(permission.getHora_fin());
                dto.setTipo_expiracion(permission.getTipo_expiracion());

                response.add(dto);
            });

            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    response
            );
        }catch (Exception e){
            return GeneralResponse.getResponse(
            HttpStatus.BAD_REQUEST,
            "Error when creating permission",
            e.getMessage()
        );
            }
    }

    @GetMapping("/permission-all")
    public ResponseEntity<GeneralResponse> CreatePermission(){
        try{
            List<Permission> list = permissionService.GetAllPermissions();
            List<PermissionResponseDTO> response = new ArrayList<>();

            if (list.isEmpty()){
                list.add(null);
                return GeneralResponse.getResponse(
                        HttpStatus.NO_CONTENT,
                        "No permissions found",
                        list
                );
            }

            list.forEach(permission -> {
                PermissionResponseDTO dto = new PermissionResponseDTO();

                dto.setId(permission.getId());
                dto.setUser(permission.getUser().getCorreo());
                dto.setHouse(permission.getHouse().getNumber());
                dto.setAprovado(permission.getAprovado());
                dto.setActivo(permission.getActivo());
                dto.setDias_semana(permission.getDias_semana());
                dto.setFecha_inicio(permission.getFecha_inicio());
                dto.setFecha_final(permission.getFecha_final());
                dto.setHora_inicio(permission.getHora_inicio());
                dto.setHora_fin(permission.getHora_fin());
                dto.setTipo_expiracion(permission.getTipo_expiracion());

                response.add(dto);
            });

            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    response
            );
        }catch (Exception e){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when getting permissions",
                    e.getMessage()
            );
        }
    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse> CreatePermission(@RequestBody @Valid RequestPermissionDTO data, BindingResult error){
        if (error.hasErrors()){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating user",
                    error.getAllErrors()
            );
        }
        try {
            User user_visit = userService.getUserByEmail(data.getEmail_permission());
            User user_house = userService.getUserByEmail(data.getEmail_house());

            if (user_visit == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "User not found: " + data.getEmail_permission()
                );
            }

            if (user_house == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "User not found: " + data.getEmail_house()
                );
            }

            permissionService.CreatePermission(user_visit, user_house, data);

            return GeneralResponse.getResponse(
                    HttpStatus.CREATED
            );

        }catch (Exception e){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating permission",
                    e.getMessage()
            );
        }
    }

    @GetMapping("/manage-permission/{number}")
    public ResponseEntity<GeneralResponse> ManagePermissions(@PathVariable String number){
        try{
            House house = houseRepository.findByNumber(number).orElse(null);
            if (house == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "House not found!"
                );
            }

            List<Permission> list = permissionService.GetPermissionsByHouse(house);
            List<PermissionResponseDTO> response = new ArrayList<>();

            if (list.isEmpty()){
                list.add(null);
                return GeneralResponse.getResponse(
                        HttpStatus.NO_CONTENT,
                        "This user does not has permissions!",
                        list
                );
            }

            list.forEach(permission -> {
                PermissionResponseDTO dto = new PermissionResponseDTO();

                dto.setId(permission.getId());
                dto.setUser(permission.getUser().getCorreo());
                dto.setHouse(permission.getHouse().getNumber());
                dto.setAprovado(permission.getAprovado());
                dto.setActivo(permission.getActivo());
                dto.setDias_semana(permission.getDias_semana());
                dto.setFecha_inicio(permission.getFecha_inicio());
                dto.setFecha_final(permission.getFecha_final());
                dto.setHora_inicio(permission.getHora_inicio());
                dto.setHora_fin(permission.getHora_fin());
                dto.setTipo_expiracion(permission.getTipo_expiracion());

                response.add(dto);
            });

            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    response
            );
        }catch (Exception e){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when getting permission",
                    e.getMessage()
            );
        }
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<GeneralResponse> ApprovePermission(@PathVariable Long id){
        try {
            Permission permission = permissionRepository.findById(id).orElse(null);
            if (permission == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "Permission not found"
                );
            }

            permissionService.SetPermissionApproveAndActive(permission);

            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    "Permission approved!"
            );

        }catch (Exception e){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when approving permission",
                    e.getMessage()
            );
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GeneralResponse> DeletePermission(@PathVariable Long id){
        try {
            Permission permission = permissionRepository.findById(id).orElse(null);
            if (permission == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "Permission not found"
                );
            }

            permissionService.DeletePermission(permission);

            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    "Permission deleted!"
            );

        }catch (Exception e){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when deleting permission",
                    e.getMessage()
            );
        }
    }
}
