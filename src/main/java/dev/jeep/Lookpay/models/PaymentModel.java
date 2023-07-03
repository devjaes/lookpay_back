package dev.jeep.Lookpay.models;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "payments")
@AllArgsConstructor
@RequiredArgsConstructor
public class PaymentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Timestamp paymentDate;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String companyRuc;

    @Column(nullable = false)
    private String companyAccountName;

    @Column(nullable = false)
    private String companyAccountNumber;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private String clientDNI;

    @Column(nullable = false)
    private String clientAccountName;

    @Column(nullable = false)
    private String clientAccountNumber;

}
