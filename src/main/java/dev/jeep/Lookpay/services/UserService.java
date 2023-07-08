package dev.jeep.Lookpay.services;

import java.sql.Date;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import dev.jeep.Lookpay.dtos.ResetPasswordDTO;
import dev.jeep.Lookpay.dtos.ResetRequestPasswordDTO;
import dev.jeep.Lookpay.dtos.UserRegisterDTO;
import dev.jeep.Lookpay.dtos.UserResponseDTO;
import dev.jeep.Lookpay.dtos.UserUpdateDTO;
import dev.jeep.Lookpay.enums.RolEnum;
import dev.jeep.Lookpay.models.CityModel;
import dev.jeep.Lookpay.models.ClientModel;
import dev.jeep.Lookpay.models.CompanyModel;
import dev.jeep.Lookpay.models.UserModel;
import dev.jeep.Lookpay.repository.CityRepository;
import dev.jeep.Lookpay.repository.ClientRepository;
import dev.jeep.Lookpay.repository.CompanyRepository;
import dev.jeep.Lookpay.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CompanyRepository companyRepository;

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

    public ResponseEntity<LinkedHashMap<String, Object>> update(Long userId, UserUpdateDTO userUpdate) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        UserModel user = userRepository.getByUserId(userId);
        try {

            if (user == null) {
                response.put("message", "User not found");
                response.put("status", HttpStatus.NOT_FOUND.value());

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
            }

            if (userUpdate.getPhoneNumber() != null && !userUpdate.getPhoneNumber().equals("")) {
                user.setPhoneNumber(userUpdate.getPhoneNumber());
            }

            if (userUpdate.getPassword() != null && !userUpdate.getPassword().equals("")) {
                Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
                String hash = argon2.hash(1, 1024, 1, userUpdate.getPassword());
                user.setPassword(hash);
            }

            if (userUpdate.getEmail() != null && !userUpdate.getEmail().equals("")) {
                if (!userUpdate.getEmail().equals(user.getEmail())) {

                    if (this.validateIfExists(userUpdate.getEmail())) {
                        response.put("message", "Email already exist");
                        response.put("status", HttpStatus.BAD_REQUEST.value());

                        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
                    }
                }
                user.setEmail(userUpdate.getEmail());
            }

            if (userUpdate.getAddress() != null && userUpdate.getAddress() != "") {
                user.setAddress(userUpdate.getAddress());
            }

            if (userUpdate.getCityId() != null && userUpdate.getCityId().compareTo(Long.valueOf("0")) != 0) {
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
        } catch (Exception e) {
            response.put("message", "Error updating user");
            response.put("error", e.getCause());
            response.put("user", userUpdate.getCityId().compareTo(Long.valueOf("0")) != 0);
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<LinkedHashMap<String, Object>> resetRequestPassword(
            ResetRequestPasswordDTO resetPasswordDTO) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        UserModel user = userRepository.getByEmail(resetPasswordDTO.getEmail());

        if (user == null) {
            response.put("message", "User not found");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            if (resetPasswordDTO.getType().equals("CLIENT")) {
                ClientModel client = clientRepository.findById(user.getClient().getId()).get();

                if (client == null) {
                    response.put("message", "Client not found");
                    response.put("status", HttpStatus.NOT_FOUND.value());

                    return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
                }

                if (!client.getDni().equals(resetPasswordDTO.getDni_ruc())
                        || !client.getBirthDate().equals(Date.valueOf(resetPasswordDTO.getOriginDate()))
                        || !user.getPhoneNumber().equals(resetPasswordDTO.getPhoneNumber())
                        || !user.getName().equals(resetPasswordDTO.getName())) {

                    response.put("message", "Invalid data");
                    response.put("status", HttpStatus.BAD_REQUEST.value());

                    return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);

                }

                response.put("message", "Password can be reset");
                response.put("userId", user.getId());
                response.put("status", HttpStatus.OK.value());

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);

            } else {
                CompanyModel company = companyRepository.findById(user.getCompany().getId()).get();

                if (company == null) {
                    response.put("message", "Company not found");
                    response.put("status", HttpStatus.NOT_FOUND.value());

                    return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
                }

                if (!company.getRuc().equals(resetPasswordDTO.getDni_ruc())
                        || !company.getFundationDate().equals(Date.valueOf(resetPasswordDTO.getOriginDate()))
                        || !user.getPhoneNumber().equals(resetPasswordDTO.getPhoneNumber())
                        || !user.getName().equals(resetPasswordDTO.getName())) {

                    response.put("message", "Invalid data");
                    response.put("status", HttpStatus.BAD_REQUEST.value());

                    return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
                }

                response.put("message", "Password can be reset");
                response.put("userId", user.getId());
                response.put("status", HttpStatus.OK.value());

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
            }

        } catch (Exception e) {
            response.put("message", "Error ocurred in the server");
            response.put("status", HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<LinkedHashMap<String, Object>> resetPassword(Long id, ResetPasswordDTO resetPasswordDTO) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        UserModel user = userRepository.findById(id).get();

        try {
            if (user == null) {
                response.put("message", "User not found");
                response.put("status", HttpStatus.NOT_FOUND.value());

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
            }

            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
            String hash = argon2.hash(1, 1024, 1, resetPasswordDTO.getNewPassword());
            user.setPassword(hash);

            userRepository.save(user);

            response.put("message", "Password updated successfully");
            response.put("password", resetPasswordDTO.getNewPassword());
            response.put("status", HttpStatus.OK.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("message", "Error ocurred in the server");
            response.put("status", HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
