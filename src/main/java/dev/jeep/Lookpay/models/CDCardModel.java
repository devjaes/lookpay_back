package dev.jeep.Lookpay.models;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

import dev.jeep.Lookpay.enums.CardTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "cd_cards")
@Data
@AllArgsConstructor
@RequiredArgsConstructor

public class CDCardModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CardTypeEnum cardType;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(nullable = false)
    private String cardHolderName;

    @Column(nullable = false)
    private String ccv;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM")
    private String expirationDate;

    @Column(nullable = false)
    private Double balance;

    @OneToOne(mappedBy = "cdCard")
    private PaymentMethodModel paymentMethod;
}
