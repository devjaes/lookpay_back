package dev.jeep.Lookpay.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.jeep.Lookpay.dtos.BankCoopAccountResponseDTO;
import dev.jeep.Lookpay.dtos.CardResponseDTO;
import dev.jeep.Lookpay.dtos.ClientResponseDTO;
import dev.jeep.Lookpay.dtos.UserRegisterDTO;
import dev.jeep.Lookpay.enums.GenderEnum;
import dev.jeep.Lookpay.models.BankCoopAccountModel;
import dev.jeep.Lookpay.models.CDCardModel;
import dev.jeep.Lookpay.models.ClientModel;
import dev.jeep.Lookpay.models.PaymentMethodModel;
import dev.jeep.Lookpay.models.UserModel;
import dev.jeep.Lookpay.repository.ClientRepository;
import dev.jeep.Lookpay.repository.UserRepository;

@Service
public class ClientService {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BankAccountService bankCoopAccountService;

    @Autowired
    private CardService cardService;

    public ResponseEntity<LinkedHashMap<String, Object>> register(UserRegisterDTO userDto) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            if (this.validateIfExists(userDto.getDni_ruc())) {
                response.put("message", "Client already exist");
                response.put("status", HttpStatus.BAD_REQUEST.value());

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
            }

            ClientModel newClient = new ClientModel(null, userDto.getDni_ruc(),
                    GenderEnum.getGenderEnum(userDto.getGender()),
                    Date.valueOf(userDto.getOriginDate()), null, null, null);

            ResponseEntity<LinkedHashMap<String, Object>> userResponse = userService.register(userDto);

            if (userResponse.getStatusCode().value() != HttpStatus.CREATED.value()) {
                return userResponse;
            }

            UserModel user = userService.getByEmail(userDto.getEmail());
            newClient.setUser(user);

            clientRepository.save(newClient);

            ClientResponseDTO clientDto = this.convertModelToDto(newClient);

            response.put("message", "Client created successfully");
            response.put("status", HttpStatus.CREATED.value());
            response.put("client", clientDto);

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("message", "Error creating client");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

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

    ClientModel getByDni(String dni) {
        return clientRepository.getByDni(dni);
    }

    ClientModel getById(Long id) {
        return clientRepository.findById(id).get();
    }

    public ResponseEntity<LinkedHashMap<String, Object>> delete(Long id) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        ClientModel client = clientRepository.findById(id).get();

        if (client == null) {
            response.put("message", "Client not found");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            List<CDCardModel> cards = cardService.getAllUserCards(id);

            for (CDCardModel card : cards) {
                cardService.deleteCard(card.getId());
            }

            List<BankCoopAccountModel> accounts = bankCoopAccountService.getAllBankAccountsByClientId(id);

            for (BankCoopAccountModel account : accounts) {
                bankCoopAccountService.deleteBankAccount(account.getId());
            }

            clientRepository.delete(client);

            response.put("message", "Client deleted successfully");
            response.put("status", HttpStatus.OK.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("message", "Error deleting client");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<LinkedHashMap<String, Object>> setPreferedPayment(Long clientId, Long id, String type) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        ClientModel client = clientRepository.findById(clientId).get();

        if (client == null) {
            response.put("message", "Client not found");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            if (type.equals("CARD")) {
                CDCardModel card = cardService.getCardById(id);
                PaymentMethodModel paymentMethod = card.getPaymentMethod();

                client.setPreferedAccount(paymentMethod);
                clientRepository.save(client);

                CardResponseDTO cardDto = new CardResponseDTO();
                cardDto.setId(card.getId());
                cardDto.setName(card.getPaymentMethod().getName());
                cardDto.setNumber(card.getNumber());
                cardDto.setCardType(card.getCardType().toString());
                cardDto.setCardHolderName(card.getCardHolderName());
                cardDto.setCvv(card.getCvv());
                cardDto.setExpirationDate(card.getExpirationDate());

                response.put("message", "Prefered payment method set successfully");
                response.put("status", HttpStatus.OK.value());
                response.put("account", cardDto);

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
            }

            if (type.equals("ACCOUNT")) {
                BankCoopAccountModel account = bankCoopAccountService.getAccountById(id);
                PaymentMethodModel paymentMethod = account.getPaymentMethod();

                client.setPreferedAccount(paymentMethod);
                clientRepository.save(client);

                BankCoopAccountResponseDTO accountDto = new BankCoopAccountResponseDTO();
                accountDto.setId(account.getId());
                accountDto.setName(account.getPaymentMethod().getName());
                accountDto.setNumber(account.getAccountNumber());
                accountDto.setAccountType(account.getAccountType().toString());
                accountDto.setBankName(account.getBankName().toString());
                accountDto.setAccountHolderName(account.getAccountHolderName());
                accountDto.setAccountHolderDNI(account.getAccountHolderDNI());
                accountDto.setAccountHolderEmail(account.getAccountHolderEmail());
                accountDto.setAccountPassword(account.getAccountPassword());

                response.put("message", "Prefered payment method set successfully");
                response.put("status", HttpStatus.OK.value());
                response.put("account", accountDto);

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
            }

            response.put("message", "Invalid payment method type");
            response.put("status", HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            response.put("message", "Error setting prefered payment method");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getPreferedPayment(Long clientId) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        ClientModel client = clientRepository.findById(clientId).get();

        if (client == null) {
            response.put("message", "Client not found");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            PaymentMethodModel paymentMethod = client.getPreferedAccount();

            if (paymentMethod == null) {
                response.put("message", "Client has no prefered payment method");
                response.put("status", HttpStatus.NOT_FOUND.value());

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
            }

            if (paymentMethod.getCdCard() != null) {
                CDCardModel card = paymentMethod.getCdCard();

                CardResponseDTO cardDto = new CardResponseDTO();
                cardDto.setId(card.getId());
                cardDto.setName(card.getPaymentMethod().getName());
                cardDto.setNumber(card.getNumber());
                cardDto.setCardType(card.getCardType().toString());
                cardDto.setCardHolderName(card.getCardHolderName());
                cardDto.setCvv(card.getCvv());
                cardDto.setExpirationDate(card.getExpirationDate());

                response.put("message", "Prefered payment method found");
                response.put("status", HttpStatus.OK.value());
                response.put("account", cardDto);

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
            }

            if (paymentMethod.getBankAccount() != null) {
                BankCoopAccountModel account = paymentMethod.getBankAccount();

                BankCoopAccountResponseDTO accountDto = new BankCoopAccountResponseDTO();
                accountDto.setId(account.getId());
                accountDto.setName(account.getPaymentMethod().getName());
                accountDto.setNumber(account.getAccountNumber());
                accountDto.setAccountType(account.getAccountType().toString());
                accountDto.setBankName(account.getBankName().toString());
                accountDto.setAccountHolderName(account.getAccountHolderName());
                accountDto.setAccountHolderDNI(account.getAccountHolderDNI());
                accountDto.setAccountHolderEmail(account.getAccountHolderEmail());
                accountDto.setAccountPassword(account.getAccountPassword());

                response.put("message", "Prefered payment method found");
                response.put("status", HttpStatus.OK.value());
                response.put("account", accountDto);

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
            }

            response.put("message", "Client has no prefered payment method");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            response.put("message", "Error getting prefered payment method for client");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
