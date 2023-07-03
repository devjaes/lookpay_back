package dev.jeep.Lookpay.models;

import java.sql.Date;
import java.util.List;

import dev.jeep.Lookpay.enums.GenderEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "clients")
@Data
@AllArgsConstructor
@RequiredArgsConstructor

public class ClientModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String dni;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(nullable = false)
    private Date birthDate;

    @OneToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel user;

    @OneToMany()
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private List<PaymentMethodModel> paymentMethods;

    @OneToOne()
    @JoinColumn(name = "prefered_account_id", referencedColumnName = "id")
    private PaymentMethodModel preferedAccount;
}
