package com.dev.hlvsbackend.controllers;

import com.dev.hlvsbackend.domain.dtos.Entrance.RegisterAnonymousEntranceDTO;
import com.dev.hlvsbackend.domain.dtos.Entrance.ResponseEntranceDTO;
import com.dev.hlvsbackend.domain.dtos.GeneralResponse;
import com.dev.hlvsbackend.domain.dtos.User.RegisterUserDTO;
import com.dev.hlvsbackend.domain.entities.Entrance;
import com.dev.hlvsbackend.domain.entities.Terminal;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.repositories.TerminalRepository;
import com.dev.hlvsbackend.repositories.UserRepository;
import com.dev.hlvsbackend.services.EntranceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/residential/entrance")
public class EntranceController {
    private final EntranceService entranceService;
    private final TerminalRepository terminalRepository;
    private final UserRepository userRepository;

    public EntranceController(
            EntranceService entranceService,
            TerminalRepository terminalRepository,
            UserRepository userRepository
    ){
        this.entranceService = entranceService;
        this.terminalRepository = terminalRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/anonymous-access")
    public ResponseEntity<GeneralResponse> RegisterAnonymousEntrance(@RequestBody @Valid RegisterAnonymousEntranceDTO data, BindingResult error){
        if (error.hasErrors()){
            return GeneralResponse.getResponse(
                    HttpStatus.BAD_REQUEST,
                    "Error when creating user",
                    error.getAllErrors()
            );
        }

        try{
            Terminal terminal = terminalRepository.findByUbicacion(data.getType().toLowerCase()).orElse(null);

            Entrance response = entranceService.registerAnonymousEntrance(data, terminal);

            if (terminal == null){
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "Terminal not found",
                        response
                );
            }

            return GeneralResponse.getResponse(
                    HttpStatus.CREATED,
                    "Entrance registered successful",
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

    @GetMapping("/all")
    public ResponseEntity<GeneralResponse> findAll() {
        List<Entrance> list = entranceService.getAllEntrances();
        List<ResponseEntranceDTO> response = new ArrayList<>();

        list.forEach(entrance -> {
            ResponseEntranceDTO dto = new ResponseEntranceDTO();
            dto.setId(entrance.getId());
            if (entrance.getCasa() != null)
                dto.setHouse(entrance.getCasa().getNumber());
            if (entrance.getId_usuario() != null) {
                dto.setEmail(entrance.getId_usuario().getCorreo());
            }
            else{
                dto.setEmail(entrance.getUser_name_anonymous());
            }
            if (entrance.getComentario() != null)
                dto.setComment(entrance.getComentario());
            dto.setHour(entrance.getHora().toString());
            dto.setDate(entrance.getFecha().toString());
            dto.setType(entrance.getEntrance_type());

            response.add(dto);
        });

        return GeneralResponse.getResponse(
                HttpStatus.OK,
                "List of entrances!",
                response
        );
    }

    @GetMapping("/all/{email}")
    public ResponseEntity<GeneralResponse> findAllByUser(@PathVariable String email) {
        User user = userRepository.findUserByCorreo(email).orElse(null);

        if (user == null){
            return GeneralResponse.getResponse(
                    HttpStatus.NOT_FOUND,
                    "User not found"
            );
        }

        List<Entrance> list = user.getEntradas();

        if (user.getEntradas() == null){
            return GeneralResponse.getResponse(
                    HttpStatus.NOT_FOUND,
                    "User does not has entrances"
            );
        }

        List<ResponseEntranceDTO> response = new ArrayList<>();

        list.forEach(entrance -> {
            ResponseEntranceDTO dto = new ResponseEntranceDTO();
            dto.setId(entrance.getId());
            if (entrance.getCasa() != null)
                dto.setHouse(entrance.getCasa().getNumber());
            if (entrance.getId_usuario() != null) {
                dto.setEmail(entrance.getId_usuario().getCorreo());
            }
            else{
                dto.setEmail(entrance.getUser_name_anonymous());
            }
            if (entrance.getComentario() != null)
                dto.setComment(entrance.getComentario());
            dto.setHour(entrance.getHora().toString());
            dto.setDate(entrance.getFecha().toString());
            dto.setType(entrance.getEntrance_type());

            response.add(dto);
        });

        return GeneralResponse.getResponse(
                HttpStatus.OK,
                "List of entrances!",
                response
        );
    }
}
