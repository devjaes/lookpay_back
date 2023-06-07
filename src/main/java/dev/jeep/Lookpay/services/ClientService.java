package dev.jeep.Lookpay.services;

import java.sql.Timestamp;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.jeep.Lookpay.dtos.UserRegisterDTO;
import dev.jeep.Lookpay.enums.GenderEnum;
import dev.jeep.Lookpay.models.ClientModel;
import dev.jeep.Lookpay.models.UserModel;
import dev.jeep.Lookpay.repository.ClientRepository;

@Service
public class ClientService {
    @Autowired
    private UserService userService;

    @Autowired
    private ClientRepository clientRepository;

    public ResponseEntity<LinkedHashMap<String, Object>> register(UserRegisterDTO userDto, String dni, String gender,
            String birthDate) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        ResponseEntity<LinkedHashMap<String, Object>> userResponse = userService.register(userDto);

        if (userResponse.getStatusCode().value() != HttpStatus.CREATED.value()) {
            return userResponse;
        }

        UserModel user = userService.convertDtoToModel(userDto);

        ClientModel newClient = new ClientModel(null, dni, GenderEnum.getGenderEnum(gender),
                Timestamp.valueOf(birthDate), user, null, null);
        clientRepository.save(newClient);

        response.put("message", "Client created successfully");
        response.put("status", HttpStatus.CREATED.value());
        response.put("client", newClient);

        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.CREATED);


    }
}
