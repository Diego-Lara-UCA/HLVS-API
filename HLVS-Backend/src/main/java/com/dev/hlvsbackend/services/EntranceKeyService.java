package com.dev.hlvsbackend.services;

import com.dev.hlvsbackend.domain.dtos.EntranceKey.EntranceKeyResponseDTO;
import com.dev.hlvsbackend.domain.entities.Entrance_Key;
import com.dev.hlvsbackend.domain.entities.Permission;
import com.dev.hlvsbackend.domain.entities.Terminal;

public interface EntranceKeyService {
    EntranceKeyResponseDTO CreateEntranceKey(Permission permission);
    Entrance_Key getEntranceKeyByKey(String key);
    void SetTerminalToEntranceKey(Entrance_Key entranceKey, Terminal terminal);

    void SetKeyExpired(Entrance_Key key);
}
