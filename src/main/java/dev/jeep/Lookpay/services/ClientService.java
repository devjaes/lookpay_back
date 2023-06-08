package dev.jeep.Lookpay.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
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
import dev.jeep.Lookpay.utils.DateUtil;

@Service
public class ClientService {
    @Autowired
    private UserService userService;

    @Autowired
    private ClientRepository clientRepository;

    public ResponseEntity<LinkedHashMap<String, Object>> register(UserRegisterDTO userDto, String dni, String gender,
            String birthDate) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        if (this.validateIfExists(dni)) {
            response.put("message", "Client already exist");
            response.put("status", HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<LinkedHashMap<String, Object>> userResponse = userService.register(userDto);

        if (userResponse.getStatusCode().value() != HttpStatus.CREATED.value()) {
            return userResponse;
        }

        UserModel user = userService.convertDtoToModel(userDto);

        ClientModel newClient = new ClientModel(null, dni, GenderEnum.getGenderEnum(gender),
                Timestamp.valueOf(DateUtil.transformWebDateToDBDate(birthDate)), user, null, null);
        clientRepository.save(newClient);

        response.put("message", "Client created successfully");
        response.put("status", HttpStatus.CREATED.value());
        response.put("client", newClient);

        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.CREATED);
    }

    public boolean validateIfExists(String dni) {
        ClientModel client = clientRepository.getByDni(dni);
        return client != null;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getClientById(String rol, Long clientId) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        if (!RolEnum.getRolEnum(rol).equals(RolEnum.ADMIN)) {
            response.put("message", "You don't have permission to do this");
            response.put("status", HttpStatus.UNAUTHORIZED.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.UNAUTHORIZED);
        }

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
