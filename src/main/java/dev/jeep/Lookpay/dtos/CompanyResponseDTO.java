package dev.jeep.Lookpay.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CompanyResponseDTO {
    private Long id;
    private String name;
    private String ruc;
    private String email;
    private String phoneNumber;
    private String city;
    private String province;
    private String address;
}
