package dev.jeep.Lookpay.controllers;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jeep.Lookpay.dtos.BankCoopAccountResponseDTO;
import dev.jeep.Lookpay.dtos.CardResponseDTO;
import dev.jeep.Lookpay.dtos.PaymentMethodRegisterDTO;
import dev.jeep.Lookpay.services.PaymentMethodService;
import org.springframework.web.bind.annotation.GetMapping;

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

    @PutMapping(value = "/card")
    public ResponseEntity<LinkedHashMap<String, Object>> updateCard(
            @RequestBody CardResponseDTO cardResponseDTO) {
        return paymentMethodService.responseUpdateCard(cardResponseDTO);
    }

    @PutMapping(value = "/account")
    public ResponseEntity<LinkedHashMap<String, Object>> updateAccount(
            @RequestBody BankCoopAccountResponseDTO bankCoopAccountResponseDTO) {
        return paymentMethodService.responseUpdateBankAccount(bankCoopAccountResponseDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> deletePaymentMethod(@PathVariable("id") Long id,
            @RequestParam("type") String type) {
        return paymentMethodService.deletePaymentMethod(id, type);
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> getTypePaymentMethodsByClientId(@PathVariable("id") Long id) {
        return paymentMethodService.getTypePaymentMethodsByClientId(id);
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> getTypePaymentMethodsByCompanyId(@PathVariable("id") Long id) {
        return paymentMethodService.getTypePaymentMethodsByCompanyId(id);
    }

}
