package dev.jeep.Lookpay.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ClientResponseDTO {
    private Long id;
    private String name;
    private String dni;
    private String gender;
    private String email;
    private String phoneNumber;
    private String city;
    private String province;
    private String address;

}
