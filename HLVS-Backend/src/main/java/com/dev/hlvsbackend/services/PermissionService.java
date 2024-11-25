package com.dev.hlvsbackend.services;

import com.dev.hlvsbackend.domain.dtos.Permission.PermissionResponseDTO;
import com.dev.hlvsbackend.domain.dtos.Permission.RegisterResidentPermissionDTO;
import com.dev.hlvsbackend.domain.dtos.Permission.RequestPermissionDTO;
import com.dev.hlvsbackend.domain.entities.House;
import com.dev.hlvsbackend.domain.entities.Permission;
import com.dev.hlvsbackend.domain.entities.User;

import java.util.List;

public interface PermissionService {
    void CreatePermissionSupervisor(User user, House house);
    void CreatePermissionGuest(User user, House house);
    void RequestPermission(User user_visit, User user_house,RequestPermissionDTO data);
    void CreatePermission(User user_visit, User user_house, RequestPermissionDTO data);
    List<Permission> GetAllPermissions();
    List<Permission> GetPermissionsByHouse(House house);
    List<Permission> GetPermissionsApprovedByUser(User user);
    List<Permission> GetPermissionsApprovedAndActiveByUser(User user);
    void SetPermissionInactive(Permission permission);
    void SetPermissionApproveAndActive(Permission permission);
    void DeletePermission(Permission permission);
    Permission findPermissionById(Long id);
}
