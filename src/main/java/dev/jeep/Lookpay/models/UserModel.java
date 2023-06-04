package dev.jeep.Lookpay.models;

import dev.jeep.Lookpay.enums.RolEnum;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@RequiredArgsConstructor

public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private RolEnum rol;

    @Column(nullable = true)
    private String address;

    @Column(name = "phone_number", nullable = true)
    private String phone;

    @OneToOne(mappedBy = "user")
    private ClientModel client;

}
