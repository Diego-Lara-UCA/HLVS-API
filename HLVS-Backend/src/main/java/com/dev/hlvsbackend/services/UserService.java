package com.dev.hlvsbackend.services;

import com.dev.hlvsbackend.domain.dtos.User.RegisterGuardDTO;
import com.dev.hlvsbackend.domain.dtos.User.RegisterUserDTO;
import com.dev.hlvsbackend.domain.entities.House;
import com.dev.hlvsbackend.domain.entities.Token;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.domain.enums.UserTypeE;
import com.dev.hlvsbackend.utils.UserUtils;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    String registerUser(RegisterUserDTO data);
    String registerGuard(User data);
    User getUserByEmail(String email) throws UserUtils.UserNotFoundException;
    House setHouseToUser(User user, String house_id, UserTypeE userTypeE);
    Token registerToken(User user) throws Exception;
    Boolean isTokenValid(User user, String token);
    void cleanTokens(User user) throws Exception;
    List<User> getAllGuards();
    User getUserById(String id);
}
