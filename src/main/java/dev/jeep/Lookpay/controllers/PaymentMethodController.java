package dev.jeep.Lookpay.controllers;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jeep.Lookpay.dtos.BankCoopAccountResponseDTO;
import dev.jeep.Lookpay.dtos.CardResponseDTO;
import dev.jeep.Lookpay.dtos.PaymentMethodRegisterDTO;
import dev.jeep.Lookpay.services.PaymentMethodService;

@RestController
@RequestMapping("/paymentmethod")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodService paymentMethodService;

    @PostMapping()
    public ResponseEntity<LinkedHashMap<String, Object>> registerPaymentMethod(
            @RequestBody PaymentMethodRegisterDTO paymentMethodRegister) {
        return paymentMethodService.register(paymentMethodRegister);
    }

    @PutMapping("/card")
    public ResponseEntity<LinkedHashMap<String, Object>> updateCard(
            @RequestBody CardResponseDTO cardResponseDTO) {
        return paymentMethodService.responseUpdateCard(cardResponseDTO);
    }

    @PutMapping("/account")
    public ResponseEntity<LinkedHashMap<String, Object>> updateAccount(
            @RequestBody BankCoopAccountResponseDTO bankCoopAccountResponseDTO) {
        return paymentMethodService.responseUpdateBankAccount(bankCoopAccountResponseDTO);
    }
}
