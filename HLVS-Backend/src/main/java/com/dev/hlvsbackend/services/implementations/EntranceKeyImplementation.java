package com.dev.hlvsbackend.services.implementations;

import com.dev.hlvsbackend.domain.dtos.EntranceKey.EntranceKeyResponseDTO;
import com.dev.hlvsbackend.domain.dtos.Permission.PermissionResponseDTO;
import com.dev.hlvsbackend.domain.entities.Entrance;
import com.dev.hlvsbackend.domain.entities.Entrance_Key;
import com.dev.hlvsbackend.domain.entities.Permission;
import com.dev.hlvsbackend.domain.entities.Terminal;
import com.dev.hlvsbackend.domain.enums.KeyState;
import com.dev.hlvsbackend.repositories.EntranceKeyRepository;
import com.dev.hlvsbackend.repositories.EntranceRepository;
import com.dev.hlvsbackend.repositories.GraceTimeRepository;
import com.dev.hlvsbackend.services.EntranceKeyService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class EntranceKeyImplementation implements EntranceKeyService {
    private final EntranceKeyRepository entranceKeyRepository;
    private final GraceTimeRepository graceTimeRepository;
    private final EntranceRepository entranceRepository;


    public EntranceKeyImplementation(
            EntranceKeyRepository entranceKeyRepository,
            GraceTimeRepository graceTimeRepository,
            EntranceRepository entranceRepository
    ){
        this.entranceKeyRepository = entranceKeyRepository;
        this.graceTimeRepository = graceTimeRepository;
        this.entranceRepository = entranceRepository;
    }

    @Override
    public EntranceKeyResponseDTO CreateEntranceKey(Permission permission){
        Entrance_Key newentrancekey = new Entrance_Key();
        newentrancekey.setPermission(permission);
        newentrancekey.setEstado(KeyState.ACTIVE);
        newentrancekey.setFecha_creacion(LocalDate.now(ZoneId.of("America/El_Salvador")));
        newentrancekey.setHora_creacion(LocalTime.now(ZoneId.of("America/El_Salvador")));
        newentrancekey.setKey(UUID.randomUUID().toString());
        newentrancekey.setGraceTime(graceTimeRepository.findById((long) 1).orElse(null));
        entranceKeyRepository.save(newentrancekey);

        EntranceKeyResponseDTO response = new EntranceKeyResponseDTO();

        response.setKey(newentrancekey.getKey());
        response.setCreationDate(newentrancekey.getFecha_creacion().toString());
        response.setCreationTime(newentrancekey.getHora_creacion().toString());
        response.setGraceTime(newentrancekey.getGraceTime().getTiempo().toString());

        return response;
    }

    @Override
    public Entrance_Key getEntranceKeyByKey(String key){
        return entranceKeyRepository.findByKey(key).orElse(null);
    }

    @Override
    public void SetTerminalToEntranceKey(Entrance_Key entranceKey, Terminal terminal){
        Entrance newentrance = new Entrance();
        newentrance.setTerminal(terminal);
        newentrance.setId_usuario(entranceKey.getPermission().getUser());
        if (entranceKey.getPermission().getUser().getCasa() != null)
            newentrance.setCasa(entranceKey.getPermission().getUser().getCasa());
        newentrance.setFecha(LocalDate.now(ZoneId.of("America/El_Salvador")));
        newentrance.setHora(LocalTime.now(ZoneId.of("America/El_Salvador")));
        newentrance.setEntrance_type(terminal.getUbicacion().toUpperCase());
        entranceRepository.save(newentrance);
    }

    @Override
    public void SetKeyExpired(Entrance_Key key){
        key.setEstado(KeyState.EXPIRED);
        entranceKeyRepository.save(key);
    }
}
