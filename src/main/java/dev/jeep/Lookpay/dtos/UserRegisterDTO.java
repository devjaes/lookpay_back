package dev.jeep.Lookpay.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserRegisterDTO {
    private String name;
    private String email;
    private String password;
    private String rol;
    private String address;
    private String phoneNumber;
    private String originDate;

}
