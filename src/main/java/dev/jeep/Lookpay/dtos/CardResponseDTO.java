package dev.jeep.Lookpay.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CardResponseDTO {
    private Long id;
    private String name;
    private String number;
    private String cardType;
    private String cardHolderName;
    private String cvv;
    private String expirationDate;
}
