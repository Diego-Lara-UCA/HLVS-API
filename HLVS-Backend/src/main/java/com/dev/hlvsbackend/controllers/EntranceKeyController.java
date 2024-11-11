package com.dev.hlvsbackend.controllers;

import com.dev.hlvsbackend.domain.dtos.EntranceKey.EntranceKeyResponseDTO;
import com.dev.hlvsbackend.domain.dtos.EntranceKey.RegisterEntranceKeyDTO;
import com.dev.hlvsbackend.domain.dtos.EntranceKey.VerifyEntranceKeyDTO;
import com.dev.hlvsbackend.domain.dtos.GeneralResponse;
import com.dev.hlvsbackend.domain.entities.Entrance_Key;
import com.dev.hlvsbackend.domain.entities.Permission;
import com.dev.hlvsbackend.domain.entities.Terminal;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.domain.enums.KeyState;
import com.dev.hlvsbackend.domain.enums.UserTypeE;
import com.dev.hlvsbackend.repositories.EntranceKeyRepository;
import com.dev.hlvsbackend.repositories.PermissionRepository;
import com.dev.hlvsbackend.repositories.TerminalRepository;
import com.dev.hlvsbackend.repositories.UserRepository;
import com.dev.hlvsbackend.services.EntranceKeyService;
import com.dev.hlvsbackend.services.PermissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api/entrance/key")
public class EntranceKeyController {
    private final EntranceKeyService entranceKeyService;
    private final PermissionService permissionService;
    private final UserRepository userRepository;
    private final TerminalRepository terminalRepository;

    public EntranceKeyController(
            EntranceKeyService entranceKeyService,
            UserRepository userRepository,
            PermissionService permissionService,
            TerminalRepository terminalRepository
    ){
        this.entranceKeyService = entranceKeyService;
        this.userRepository = userRepository;
        this.permissionService = permissionService;
        this.terminalRepository = terminalRepository;
    }


    @PostMapping("/create")
    public ResponseEntity<GeneralResponse> CreateKey(@RequestBody @Valid RegisterEntranceKeyDTO data, BindingResult error){
        if (error.hasErrors()){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating user",
                    error.getAllErrors()
            );
        }
        try {
            User user = userRepository.findUserByCorreo(data.getEmail()).orElse(null);
            if (user == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "User not found: " + data.getEmail()
                );
            }

            Permission permission = permissionService.GetPermissionsApprovedAndActiveByUser(user).stream().findFirst().orElse(null);
            if (permission == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "This user does not has permissions!"
                );
            }

            if (user.getUserType() == UserTypeE.SUPERVISOR || user.getUserType() == UserTypeE.USER){
                EntranceKeyResponseDTO response = entranceKeyService.CreateEntranceKey(permission);

                return GeneralResponse.getResponse(
                        HttpStatus.CREATED,
                        "Entrance key created!",
                        response
                );
            }else if (user.getUserType() == UserTypeE.GUEST){

                if (!permission.getAprovado() || !permission.getActivo()){
                    return GeneralResponse.getResponse(
                            HttpStatus.CONFLICT,
                            "This permission is not approved or active!",
                            permission.getId()
                    );
                }

                if (LocalDate.now(ZoneId.of("America/El_Salvador")).isAfter(permission.getFecha_final()) ||

                        LocalDate.now(ZoneId.of("America/El_Salvador")).isBefore(permission.getFecha_inicio())){
                    permissionService.SetPermissionInactive(permission);
                    return GeneralResponse.getResponse(
                            HttpStatus.CONFLICT,
                            "Dates out of range!"
                    );
                }

                if (LocalTime.now(ZoneId.of("America/El_Salvador")).isAfter(
                        permission.getHora_fin()
                        .plusMinutes(permission.getTiempo_de_gracia().getTiempo().getMinute()))  ||

                        LocalTime.now(ZoneId.of("America/El_Salvador")).isBefore(
                                permission.getHora_inicio()
                                .minusMinutes(permission.getTiempo_de_gracia().getTiempo().getMinute()))
                ){
                    permissionService.SetPermissionInactive(permission);
                    return GeneralResponse.getResponse(
                            HttpStatus.CONFLICT,
                            "Time out of range!"
                    );
                }

                AtomicBoolean aux = new AtomicBoolean();
                aux.set(false);
                permission.getDias_semana().forEach(day -> {
                    if (day.equalsIgnoreCase(LocalDate.now(ZoneId.of("America/El_Salvador")).getDayOfWeek().toString())){
                        aux.set(true);
                    }
                });

                if (permission.getFecha_inicio().isEqual(permission.getFecha_final())){
                    if (LocalTime.now(ZoneId.of("America/El_Salvador")).isAfter(
                            permission.getHora_fin()
                                    .plusMinutes(permission.getTiempo_de_gracia().getTiempo().getMinute()))  ||

                            LocalTime.now(ZoneId.of("America/El_Salvador")).isBefore(
                                    permission.getHora_inicio()
                                            .minusMinutes(permission.getTiempo_de_gracia().getTiempo().getMinute()))
                    ){
                        permissionService.SetPermissionInactive(permission);
                    }

                    EntranceKeyResponseDTO response = entranceKeyService.CreateEntranceKey(permission);
                    return GeneralResponse.getResponse(
                            HttpStatus.CREATED,
                            "Entrance key created!",
                            response
                    );
                }else if(aux.get()){
                    EntranceKeyResponseDTO response = entranceKeyService.CreateEntranceKey(permission);
                    return GeneralResponse.getResponse(
                            HttpStatus.CREATED,
                            "Entrance key created!",
                            response
                    );
                }
            }

            return GeneralResponse.getResponse(
                    HttpStatus.CONFLICT,
                    "Error when creating entrance key"
            );

        }catch (Exception e){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating entrance key",
                    e.getMessage()
            );
        }
    }

    @PostMapping("/verify-key")
    public ResponseEntity<GeneralResponse> VerifyKey(@RequestBody VerifyEntranceKeyDTO data){
        try {
            Entrance_Key fkey = entranceKeyService.getEntranceKeyByKey(data.getKey());
            if (fkey == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "No entrance key with code: " + fkey.getKey()
                );
            }

            if (fkey.getKey().equals(data.getKey())){
                Terminal terminal = terminalRepository.findByUbicacion(data.getTerminal().toLowerCase()).orElse(null);
                if (terminal == null){
                    return GeneralResponse.getResponse(
                            HttpStatus.NOT_FOUND,
                            "Terminal not found",
                            true
                    );
                }

                if (fkey.getEstado() == KeyState.EXPIRED){
                    return GeneralResponse.getResponse(
                            HttpStatus.CONFLICT,
                            "This key is expired!"
                    );
                }

                if (!LocalDate.now(ZoneId.of("America/El_Salvador")).isEqual(fkey.getFecha_creacion())){
                    entranceKeyService.SetKeyExpired(fkey);
                    return GeneralResponse.getResponse(
                            HttpStatus.CONFLICT,
                            "Dates out of range!"
                    );
                }

                if (LocalTime.now(ZoneId.of("America/El_Salvador")).isAfter(
                        fkey.getHora_creacion()
                                .plusMinutes(fkey.getGraceTime().getTiempo().getMinute()))  ||

                        LocalTime.now(ZoneId.of("America/El_Salvador")).isBefore(
                                fkey.getHora_creacion()
                                        .minusMinutes(fkey.getGraceTime().getTiempo().getMinute()))
                ){
                    entranceKeyService.SetKeyExpired(fkey);
                    return GeneralResponse.getResponse(
                            HttpStatus.CONFLICT,
                            "Time out of range!"
                    );
                }

                entranceKeyService.SetTerminalToEntranceKey(fkey, terminal);
                entranceKeyService.SetKeyExpired(fkey);
                return GeneralResponse.getResponse(
                        HttpStatus.OK,
                        "Valid key",
                        true
                );
            }else {
                return GeneralResponse.getResponse(
                        HttpStatus.OK,
                        "The key is not Valid",
                        false
                );
            }

        }catch (Exception e){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating entrance key",
                    e.getMessage()
            );
        }
    }
}
