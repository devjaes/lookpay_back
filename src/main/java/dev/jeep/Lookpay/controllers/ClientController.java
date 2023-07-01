package dev.jeep.Lookpay.controllers;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jeep.Lookpay.dtos.UserRegisterDTO;
import dev.jeep.Lookpay.services.ClientService;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping()
    public ResponseEntity<LinkedHashMap<String, Object>> registerClient(@RequestBody UserRegisterDTO user) {
        return clientService.register(user);
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

}
