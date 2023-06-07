package dev.jeep.Lookpay.services;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.jeep.Lookpay.dtos.UserRegisterDTO;
import dev.jeep.Lookpay.enums.RolEnum;
import dev.jeep.Lookpay.models.UserModel;
import dev.jeep.Lookpay.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<LinkedHashMap<String, Object>> register(UserRegisterDTO user) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        if (!this.validateIfExists(user.getEmail())) {
            response.put("message", "User already exist");
            response.put("status", HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        UserModel newUser = this.convertDtoToModel(user);
        userRepository.save(newUser);

        response.put("message", "User created successfully");
        response.put("status", HttpStatus.CREATED.value());
        response.put("user", newUser);

        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.CREATED);

    }

    public UserModel convertDtoToModel(UserRegisterDTO user) {
        UserModel newUser = new UserModel();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setRol(RolEnum.getRolEnum(user.getRol()));
        newUser.setAddress(user.getAddress());
        newUser.setPhoneNumber(user.getPhoneNumber());

        return newUser;
    }

    private Boolean validateIfExists(String email) {
        UserModel user = userRepository.getByEmail(email);
        return user != null;
    }
}
