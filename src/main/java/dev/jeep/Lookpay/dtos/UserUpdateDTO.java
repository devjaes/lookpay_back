package dev.jeep.Lookpay.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserUpdateDTO {
    private String rol;
    private String phoneNumber;
    private String password;
    private String email;
    private String address;
    private Long cityId;
}
