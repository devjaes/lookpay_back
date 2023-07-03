package dev.jeep.Lookpay.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PaymentMethodRegisterDTO {
    private String name;
    private boolean client;
    private boolean card;
    private String userDNI;
    private String number;
    private String type;
    private String bankName;
    private String holderName;
    private String code;
    private String expirationDate;
    private String accountHolderDNI;
    private String accountHolderEmail;

}
