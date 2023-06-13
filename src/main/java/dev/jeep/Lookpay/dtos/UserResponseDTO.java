package dev.jeep.Lookpay.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String dni_ruc;
    private String rol;
    private String city;
    private String province;
    private String address;
    private String phoneNumber;
    private String originDate;
    private String gender;

}
