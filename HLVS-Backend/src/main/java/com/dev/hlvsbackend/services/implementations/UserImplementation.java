package com.dev.hlvsbackend.services.implementations;

import com.dev.hlvsbackend.domain.dtos.User.RegisterGuardDTO;
import com.dev.hlvsbackend.domain.dtos.User.RegisterUserDTO;
import com.dev.hlvsbackend.domain.entities.House;
import com.dev.hlvsbackend.domain.entities.Token;
import com.dev.hlvsbackend.domain.entities.User;
import com.dev.hlvsbackend.domain.enums.DocumentType;
import com.dev.hlvsbackend.domain.enums.UserTypeE;
import com.dev.hlvsbackend.repositories.HouseRepository;
import com.dev.hlvsbackend.repositories.TokenRepository;
import com.dev.hlvsbackend.repositories.UserRepository;
import com.dev.hlvsbackend.services.UserService;
import com.dev.hlvsbackend.utils.JWTTools;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserImplementation implements UserService {
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final TokenRepository tokenRepository;

    @Autowired
    private JWTTools jwtTools;

    public UserImplementation(
            UserRepository userRepository,
            HouseRepository houseRepository,
            TokenRepository tokenRepository
    ) {
        this.userRepository = userRepository;
        this.houseRepository = houseRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Token registerToken(User user) throws Exception {
        cleanTokens(user);

        String tokenString = jwtTools.generateToken(user);
        Token token = new Token(tokenString, user);

        tokenRepository.save(token);

        return token;
    }

    @Override
    public Boolean isTokenValid(User user, String token) {
        try {
            cleanTokens(user);
            List<Token> tokens = tokenRepository.findByUserAndActive(user, true);

            tokens.stream()
                    .filter(tk -> tk.getContent().equals(token))
                    .findAny()
                    .orElseThrow(() -> new Exception());

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void cleanTokens(User user) throws Exception {
        List<Token> tokens = tokenRepository.findByUserAndActive(user, true);
        tokens.forEach(token -> {
            if (!jwtTools.verifyToken(token.getContent())) {
                token.setActive(false);
                tokenRepository.save(token);
            }
        });
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public String registerUser(RegisterUserDTO data) throws IllegalArgumentException{
        User newUser = new User();
        newUser.setNombre(data.getName());
        newUser.setCorreo(data.getEmail());
        newUser.setUserType(UserTypeE.valueOf(data.getUserType()));
        newUser.setNumero_documento(data.getDocumentNumber());
        newUser.setTipo_documento(DocumentType.valueOf(data.getDocumentType()));

        userRepository.save(newUser);
        return "Register successful!";
    }

    @Override
    public House setHouseToUser(User user, String house_id, UserTypeE userTypeE){
        House house = houseRepository.findByNumber(house_id).orElse(null);
         if (house == null){
             return null;
         }

         user.setCasa(house);
         user.setUserType(userTypeE);
         userRepository.save(user);
         return house;
    }

    @Override
    public User getUserByEmail(String email){return userRepository.findUserByCorreo(email).orElse(null);}

    @Override
    public String registerGuard(User data){
        data.setUserType(UserTypeE.GUARD);
        userRepository.save(data);
        return "Register successful!";
    }

    @Override
    public List<User> getAllGuards(){
        return  userRepository.findUsersByUserType(UserTypeE.GUARD).orElse(null);
    }

    @Override
    public User getUserById(String id){
        return userRepository.findById(UUID.fromString(id)).orElse(null);
    }
}
