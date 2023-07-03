package dev.jeep.Lookpay.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PaymentCreationDTO {
    private String name;
    private Double amount;
    private String description;
    private String paymentDate;
    private String companyName;
    private String companyRuc;
    private String companyAccountName;
    private String companyAccountNumber;
    private String clientDNI;

}
