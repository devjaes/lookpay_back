package dev.jeep.Lookpay.models;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "companies")
@Data
@AllArgsConstructor
@RequiredArgsConstructor

public class CompanyModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ruc;

    @Column(nullable = false)
    private Date fundationDate;

    @OneToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel user;

    @OneToMany()
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private List<PaymentMethodModel> paymentMethods;

    @OneToOne()
    @JoinColumn(name = "prefered_account_id", referencedColumnName = "id")
    private PaymentMethodModel preferedAccount;
}
