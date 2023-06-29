package dev.jeep.Lookpay.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter

@Getter

@AllArgsConstructor

@RequiredArgsConstructor

@ToString

public class LoginDTO {
    private String email;
    private String password;
}
