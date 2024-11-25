package com.dev.hlvsbackend.services.implementations;

import com.dev.hlvsbackend.domain.dtos.Permission.PermissionResponseDTO;
import com.dev.hlvsbackend.domain.dtos.Permission.RegisterResidentPermissionDTO;
import com.dev.hlvsbackend.domain.dtos.Permission.RequestPermissionDTO;
import com.dev.hlvsbackend.domain.entities.Entrance_Key;
import com.dev.hlvsbackend.domain.entities.House;
import com.dev.hlvsbackend.domain.entities.Permission;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.domain.enums.KeyState;
import com.dev.hlvsbackend.repositories.EntranceKeyRepository;
import com.dev.hlvsbackend.repositories.GraceTimeRepository;
import com.dev.hlvsbackend.repositories.HouseRepository;
import com.dev.hlvsbackend.repositories.PermissionRepository;
import com.dev.hlvsbackend.services.HouseService;
import com.dev.hlvsbackend.services.PermissionService;
import com.dev.hlvsbackend.services.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PermissionImplementation implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final EntranceKeyRepository entranceKeyRepository;
    private final GraceTimeRepository graceTimeRepository;

    public PermissionImplementation(
            PermissionRepository permissionRepository,
            EntranceKeyRepository entranceKeyRepository,
            GraceTimeRepository graceTimeRepository
    ){
        this.permissionRepository = permissionRepository;
        this.entranceKeyRepository = entranceKeyRepository;
        this.graceTimeRepository = graceTimeRepository;
    }

    @Override
    public void CreatePermissionSupervisor(User user, House house){
        Permission newpermision = new Permission();
        newpermision.setUser(user);
        newpermision.setHouse(house);
        newpermision.setActivo(true);
        newpermision.setAprovado(true);
        newpermision.setTiempo_de_gracia(graceTimeRepository.findById((long) 1).orElse(null));
        permissionRepository.save(newpermision);
    }

    @Override
    public void CreatePermissionGuest(User user, House house){
        Permission newpermision = new Permission();
        newpermision.setUser(user);
        newpermision.setHouse(house);
        newpermision.setActivo(true);
        newpermision.setAprovado(true);
        newpermision.setTiempo_de_gracia(graceTimeRepository.findById((long) 1).orElse(null));
        permissionRepository.save(newpermision);
    }

    @Override
    public void RequestPermission(User user_visit, User user_house, RequestPermissionDTO data){
        Permission newpermission = new Permission();
        newpermission.setActivo(false);
        newpermission.setAprovado(false);
        newpermission.setUser(user_visit);
        newpermission.setTiempo_de_gracia(graceTimeRepository.findById((long) 1).orElse(null));
        newpermission.setDias_semana(data.getDays());
        newpermission.setFecha_inicio(LocalDate.parse(data.getFirstDate()));
        newpermission.setFecha_final(LocalDate.parse(data.getSecondDate()));
        newpermission.setHora_inicio(LocalTime.parse(data.getInitialHour()));
        newpermission.setHora_fin(LocalTime.parse(data.getFinalHour()));
        newpermission.setTipo_expiracion(data.getExpirationType());
        newpermission.setHouse(user_house.getCasa());

        permissionRepository.save(newpermission);
    }

    @Override
    public void CreatePermission(User user_visit, User user_house, RequestPermissionDTO data){
        Permission newpermission = new Permission();
        newpermission.setActivo(true);
        newpermission.setAprovado(true);
        newpermission.setUser(user_visit);
        newpermission.setTiempo_de_gracia(graceTimeRepository.findById((long) 1).orElse(null));
        newpermission.setDias_semana(data.getDays());
        newpermission.setFecha_inicio(LocalDate.parse(data.getFirstDate()));
        newpermission.setFecha_final(LocalDate.parse(data.getSecondDate()));
        newpermission.setHora_inicio(LocalTime.parse(data.getInitialHour()));
        newpermission.setHora_fin(LocalTime.parse(data.getFinalHour()));
        newpermission.setTipo_expiracion(data.getExpirationType());
        newpermission.setHouse(user_house.getCasa());

        permissionRepository.save(newpermission);
    }

    @Override
    public List<Permission> GetPermissionsApprovedByUser(User user){
        return permissionRepository.findByUserAndAprovadoIsTrue(user).orElse(null);
    }

    @Override
    public void SetPermissionInactive(Permission permission){
        permission.setActivo(false);
        permissionRepository.save(permission);
    }

    @Override
    public List<Permission> GetPermissionsApprovedAndActiveByUser(User user){
        return permissionRepository.findByUserAndAprovadoIsTrueAndActivoIsTrue(user).orElse(null);
    }

    @Override
    public List<Permission> GetAllPermissions(){
        return permissionRepository.findAll();
    }

    @Override
    public List<Permission> GetPermissionsByHouse(House house){
        return permissionRepository.findByHouse(house).orElse(null);
    }

    @Override
    public void SetPermissionApproveAndActive(Permission permission){
        permission.setActivo(true);
        permission.setAprovado(true);
        permissionRepository.save(permission);
    }

    @Override
    public void DeletePermission(Permission permission){
        permission.setActivo(false);
        permission.setAprovado(false);
        permissionRepository.save(permission);
    }

    @Override
    public Permission findPermissionById(Long id){
        return permissionRepository.findById(id).orElse(null);
    }
}
