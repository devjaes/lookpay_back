package dev.jeep.Lookpay.services;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import dev.jeep.Lookpay.enums.RolEnum;
import dev.jeep.Lookpay.models.UserModel;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    public ResponseEntity<LinkedHashMap<String, Object>> login(String email, String password) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        UserModel user = userService.getByEmail(email);

        if (user == null) {
            response.put("message", "User not found");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        if (!this.validateUser(user, password)) {
            response.put("message", "Incorrect password");
            response.put("status", HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        LinkedHashMap<String, Object> userResponse = new LinkedHashMap<>();
        userResponse.put("id", user.getId());
        userResponse.put("name", user.getName());
        userResponse.put("email", user.getEmail());
        userResponse.put("phoneNumber", user.getPhoneNumber());
        userResponse.put("address", user.getAddress());
        userResponse.put("city", user.getCity().getName());
        userResponse.put("province", user.getCity().getProvince().getName());

        if (user.getRol().equals(RolEnum.ADMIN)) {
            userResponse.put("rol", "ADMIN");
        } else if (user.getRol().equals(RolEnum.CLIENT)) {
            userResponse.put("rol", "CLIENT");
            userResponse.put("dni", user.getClient().getDni());
            userResponse.put("gender", user.getClient().getGender().toString());
        } else {
            userResponse.put("rol", "COMPANY");
            userResponse.put("ruc", user.getCompany().getRuc());
            userResponse.put("fundationDate", user.getCompany().getFundationDate().toString());
        }

        response.put("message", "User found");
        response.put("status", HttpStatus.OK.value());
        response.put("user", userResponse);

        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
    }

    public boolean validateUser(UserModel user, String loginPassword) {
        return checkPassword(loginPassword, user.getPassword());
    }

    private Boolean checkPassword(String loginPassword, String userPassword) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

        if (argon2.verify(userPassword, loginPassword)) {
            return true;
        }

        return false;
    }
}
