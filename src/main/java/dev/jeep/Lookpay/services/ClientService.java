package dev.jeep.Lookpay.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.jeep.Lookpay.dtos.ClientResponseDTO;
import dev.jeep.Lookpay.dtos.UserRegisterDTO;
import dev.jeep.Lookpay.enums.GenderEnum;
import dev.jeep.Lookpay.enums.RolEnum;
import dev.jeep.Lookpay.models.ClientModel;
import dev.jeep.Lookpay.models.UserModel;
import dev.jeep.Lookpay.repository.ClientRepository;
import dev.jeep.Lookpay.repository.UserRepository;
import dev.jeep.Lookpay.utils.DateUtil;

@Service
public class ClientService {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    public ResponseEntity<LinkedHashMap<String, Object>> register(UserRegisterDTO userDto) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        if (this.validateIfExists(userDto.getDni_ruc())) {
            response.put("message", "Client already exist");
            response.put("status", HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<LinkedHashMap<String, Object>> userResponse = userService.register(userDto);

        if (userResponse.getStatusCode().value() != HttpStatus.CREATED.value()) {
            return userResponse;
        }

        UserModel user = userService.getByEmail(userDto.getEmail());

        ClientModel newClient = new ClientModel(null, userDto.getDni_ruc(),
                GenderEnum.getGenderEnum(userDto.getGender()),
                Timestamp.valueOf(DateUtil.transformWebDateToDBDate(userDto.getOriginDate())), user, null, null);
        clientRepository.save(newClient);

        ClientResponseDTO clientDto = this.convertModelToDto(newClient);

        response.put("message", "Client created successfully");
        response.put("status", HttpStatus.CREATED.value());
        response.put("client", clientDto);

        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.CREATED);
    }

    public boolean validateIfExists(String dni) {
        ClientModel client = clientRepository.getByDni(dni);
        return client != null;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> get(Long clientId) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        ClientModel client = clientRepository.findById(clientId).get();

        if (client == null) {
            response.put("message", "Client not found");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        ClientResponseDTO clientDto = this.convertModelToDto(client);

        response.put("message", "Client found");
        response.put("status", HttpStatus.OK.value());
        response.put("client", clientDto);

        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getByUserID(Long userId) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        UserModel user = userRepository.findById(userId).get();
        ClientModel client = user.getClient();

        if (client == null) {
            response.put("message", "Client not found");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        ClientResponseDTO clientDto = this.convertModelToDto(client);

        response.put("message", "Client found");
        response.put("status", HttpStatus.OK.value());
        response.put("client", clientDto);

        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
    }

    private ClientResponseDTO convertModelToDto(ClientModel client) {
        ClientResponseDTO clientDto = new ClientResponseDTO(client.getId(), client.getUser().getName(), client.getDni(),
                client.getGender().toString(), client.getUser().getEmail(), client.getUser().getPhoneNumber(),
                client.getUser().getCity().getName(), client.getUser().getCity().getProvince().getName(),
                client.getUser().getAddress());

        return clientDto;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getAll() {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        Iterable<ClientModel> clients = clientRepository.findAll();

        if (clients == null) {
            response.put("message", "Clients not found");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        ArrayList<ClientResponseDTO> clientsDto = new ArrayList<>();

        for (ClientModel client : clients) {
            clientsDto.add(this.convertModelToDto(client));
        }

        response.put("message", "Clients found");
        response.put("status", HttpStatus.OK.value());
        response.put("clients", clientsDto);

        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
    }

}
