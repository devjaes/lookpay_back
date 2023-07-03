package dev.jeep.Lookpay.controllers;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jeep.Lookpay.dtos.PaymentCreationDTO;
import dev.jeep.Lookpay.services.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping()
    public ResponseEntity<LinkedHashMap<String, Object>> registerPayment(
            @RequestBody PaymentCreationDTO payment) {
        return paymentService.responseCreatePayment(payment);
    }
}
