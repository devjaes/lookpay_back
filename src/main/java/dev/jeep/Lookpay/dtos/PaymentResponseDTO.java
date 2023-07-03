package dev.jeep.Lookpay.dtos;

import lombok.Data;

@Data
public class PaymentResponseDTO {
    private String name;
    private Double amount;
    private String description;
    private String paymentDate;
    private String companyName;
    private String companyRuc;
    private String companyAccountName;
    private String companyAccountNumber;
    private String customerName;
    private String customerDNI;
    private String customerAccountName;
    private String customerAccountNumber;
}
