package dev.jeep.Lookpay.models;

import java.sql.Timestamp;
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
    private Timestamp originDate;

    @OneToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel user;

    @ManyToMany()
    @JoinTable(name = "company_bank_accounts", joinColumns = @JoinColumn(name = "company_id"), inverseJoinColumns = @JoinColumn(name = "bank_account_id"))
    private List<BankCoopAccountModel> bankAccounts;

}
