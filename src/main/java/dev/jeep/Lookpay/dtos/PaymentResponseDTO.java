package dev.jeep.Lookpay.dtos;

import lombok.Data;

@Data
public class PaymentResponseDTO {
    private Long id;
    private String name;
    private Double amount;
    private String description;
    private String paymentDate;
    private String companyName;
    private String companyRuc;
    private String companyAccountName;
    private String companyAccountNumber;
    private String clientName;
    private String clientDNI;
    private String clientAccountName;
    private String clientAccountNumber;
}
