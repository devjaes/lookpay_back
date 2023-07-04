package dev.jeep.Lookpay.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PaymentsMethodsResponseDTO {
    private List<BankCoopAccountResponseDTO> bankCoopCurrentsAccounts;
    private List<BankCoopAccountResponseDTO> bankCoopSavingsAccounts;
    private List<CardResponseDTO> debitCards;
    private List<CardResponseDTO> creditCards;
}
