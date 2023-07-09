package dev.jeep.Lookpay.controllers;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jeep.Lookpay.dtos.UserRegisterDTO;
import dev.jeep.Lookpay.dtos.UserUpdateDTO;
import dev.jeep.Lookpay.models.UserModel;
import dev.jeep.Lookpay.repository.ClientRepository;
import dev.jeep.Lookpay.services.ClientService;
import dev.jeep.Lookpay.services.UserService;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<LinkedHashMap<String, Object>> registerClient(@RequestBody UserRegisterDTO user) {
        return clientService.register(user);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> update(@PathVariable("id") Long id,
            @RequestBody UserUpdateDTO user) {
        try {
            UserModel userModel = clientRepository.findById(id).get().getUser();

            if (userModel == null) {
                LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                response.put("message", "User not found");

                return new ResponseEntity<>(response, org.springframework.http.HttpStatus.NOT_FOUND);
            }

            return userService.update(userModel.getId(), user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }

    }

    @GetMapping("/all")
    public ResponseEntity<LinkedHashMap<String, Object>> getAll() {
        return clientService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> get(@PathVariable("id") Long id) {
        return clientService.get(id);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> getByUserId(@PathVariable("id") Long id) {
        return clientService.getByUserID(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> delete(@PathVariable("id") Long id) {
        return clientService.delete(id);
    }

    @GetMapping("/preferedaccount/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> getPreferedPayment(@PathVariable("id") Long id) {
        return clientService.getPreferedPayment(id);
    }

    @PutMapping(value = "/preferedaccount/{clientId}/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> setPreferedPayment(@PathVariable("clientId") Long clientId,
            @PathVariable("id") Long id,
            @RequestParam("type") String type) {
        return clientService.setPreferedPayment(clientId, id, type);
    }

}
