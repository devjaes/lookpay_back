package dev.jeep.Lookpay.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import dev.jeep.Lookpay.dtos.CityDTO;
import dev.jeep.Lookpay.dtos.UserRegisterDTO;
import dev.jeep.Lookpay.dtos.UserResponseDTO;
import dev.jeep.Lookpay.dtos.UserUpdateDTO;
import dev.jeep.Lookpay.enums.RolEnum;
import dev.jeep.Lookpay.models.CityModel;
import dev.jeep.Lookpay.models.UserModel;
import dev.jeep.Lookpay.repository.CityRepository;
import dev.jeep.Lookpay.repository.UserRepository;

//Probando
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CityRepository cityRepository;

    public ResponseEntity<LinkedHashMap<String, Object>> register(UserRegisterDTO user) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        if (this.validateIfExists(user.getEmail())) {
            UserModel userExist = this.getByEmail(user.getEmail());
            response.put("message", "User already exist");
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("user", userExist.getName());

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
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(1, 1024, 1, user.getPassword());
        user.setPassword(hash);

        CityModel city = cityRepository.findById(user.getCityId()).get();

        UserModel newUser = new UserModel();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setRol(RolEnum.getRolEnum(user.getRol()));
        newUser.setCity(city);
        newUser.setAddress(user.getAddress());
        newUser.setPhoneNumber(user.getPhoneNumber());

        return newUser;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getUser(Long id) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        UserModel user = userRepository.findById(id).get();

        if (user == null) {
            response.put("message", "User not found");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());

        if (user.getRol() == RolEnum.CLIENT) {
            userResponse.setDni_ruc(user.getClient().getDni());
            userResponse.setOriginDate(user.getClient().getBirthDate().toString());
            userResponse.setGender(user.getClient().getGender().toString());

        } else {
            userResponse.setDni_ruc(user.getCompany().getRuc());
            userResponse.setOriginDate(user.getCompany().getFundationDate().toString());
        }

        userResponse.setRol(user.getRol().toString());
        userResponse.setCity(user.getCity().getName());
        userResponse.setProvince(user.getCity().getProvince().getName());
        userResponse.setAddress(user.getAddress());
        userResponse.setPhoneNumber(user.getPhoneNumber());

        response.put("message", "User found");
        response.put("status", HttpStatus.OK.value());
        response.put("user", userResponse);

        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
    }

    public UserModel getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    private Boolean validateIfExists(String email) {
        UserModel user = this.getByEmail(email);
        return user != null;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getCities() {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        List<CityModel> cities = cityRepository.findAll();
        List<CityDTO> citiesDTO = new ArrayList<CityDTO>();

        for (CityModel city : cities) {
            citiesDTO.add(new CityDTO(city.getId(), city.getName(), city.getProvince().getName()));
        }

        response.put("cities", citiesDTO);
        response.put("status", HttpStatus.OK.value());
        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
    }

    public ResponseEntity<LinkedHashMap<String, Object>> update(Long userId, UserUpdateDTO userUpdate) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        UserModel user = userRepository.findById(userId).get();

        if (user == null) {
            response.put("message", "User not found");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        if (userUpdate.getPhoneNumber() != null) {
            user.setPhoneNumber(userUpdate.getPhoneNumber());
        }

        if (userUpdate.getPassword() != null) {
            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
            String hash = argon2.hash(1, 1024, 1, userUpdate.getPassword());
            user.setPassword(hash);
        }

        if (userUpdate.getEmail() != null) {
            user.setEmail(userUpdate.getEmail());
        }

        if (userUpdate.getAddress() != null) {
            user.setAddress(userUpdate.getAddress());
        }

        if (userUpdate.getCityId() != null) {
            user.setCity(cityRepository.findById(userUpdate.getCityId()).get());
        }

        userRepository.save(user);

        LinkedHashMap<String, Object> userResponse = new LinkedHashMap<>();
        userResponse.put("id", user.getId());
        userResponse.put("name", user.getName());
        userResponse.put("email", user.getEmail());
        userResponse.put("phoneNumber", user.getPhoneNumber());
        userResponse.put("address", user.getAddress());
        userResponse.put("city", user.getCity().getName());
        userResponse.put("province", user.getCity().getProvince().getName());

        response.put("message", "User updated successfully");
        response.put("status", HttpStatus.OK.value());
        response.put("user", userResponse);

        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
    }
}
