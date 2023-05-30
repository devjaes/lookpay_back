package dev.jeep.Lookpay.models;

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

    @OneToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel user;

    @ManyToMany()
    @JoinTable(name = "client_cd_cards", joinColumns = @JoinColumn(name = "client_id"), inverseJoinColumns = @JoinColumn(name = "cd_card_id"))
    private List<CDCardModel> cdCards;

    @ManyToMany()
    @JoinTable(name = "client_bank_accounts", joinColumns = @JoinColumn(name = "client_id"), inverseJoinColumns = @JoinColumn(name = "bank_account_id"))
    private List<BankAccountModel> bankAccounts;
}
