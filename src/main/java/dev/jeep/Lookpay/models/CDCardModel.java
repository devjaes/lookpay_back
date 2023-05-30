package dev.jeep.Lookpay.models;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "cd_cards")
@AllArgsConstructor
@RequiredArgsConstructor

public class CDCardModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(nullable = false)
    private String cardHolderName;

    @Column(nullable = false)
    private String ccv;

    @Column(nullable = false)
    private String expirationDate;

    @ManyToMany(mappedBy = "cdCards")
    private List<ClientModel> clients;
}
