package dev.jeep.Lookpay.controllers;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jeep.Lookpay.dtos.UserRegisterDTO;
import dev.jeep.Lookpay.services.CompanyService;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @PostMapping()
    public ResponseEntity<LinkedHashMap<String, Object>> redisterCompany(@RequestBody UserRegisterDTO userDto) {
        return companyService.register(userDto);
    }

    @GetMapping()
    public ResponseEntity<LinkedHashMap<String, Object>> getAll() {
        return companyService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> get(Long id) {
        return companyService.get(id);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> getByUserId(Long id) {
        return companyService.getByUserId(id);
    }
}
