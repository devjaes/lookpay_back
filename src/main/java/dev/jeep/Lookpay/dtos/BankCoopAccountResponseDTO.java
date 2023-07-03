package dev.jeep.Lookpay.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BankCoopAccountResponseDTO {
    private Long id;
    private String name;
    private String number;
    private String accountType;
    private String bankName;
    private String accountHolderName;
    private String accountHolderDNI;
    private String accountHolderEmail;
    private String accountPassword;
}
