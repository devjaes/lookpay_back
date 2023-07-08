package dev.jeep.Lookpay.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "payment_methods")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PaymentMethodModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne()
    @JoinColumn(name = "bank_account_id", referencedColumnName = "id")
    private BankCoopAccountModel bankAccount;

    @OneToOne()
    @JoinColumn(name = "cd_card_id", referencedColumnName = "id")
    private CDCardModel cdCard;

    @OneToOne(mappedBy = "preferedAccount")
    @JoinColumn(name = "prefered_company_id", referencedColumnName = "id")
    private CompanyModel company;

    @OneToOne(mappedBy = "preferedAccount")
    @JoinColumn(name = "prefered_client_id", referencedColumnName = "id")
    private ClientModel client;

    @ManyToOne()
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private CompanyModel companyId;

    @ManyToOne()
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private ClientModel clientId;
}
