package dev.jeep.Lookpay.models;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cities", indexes = { @Index(name = "city_name_index", columnList = "name") })
@Data
public class CityModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserModel> users;

    @ManyToOne()
    @JoinColumn(name = "province_id")
    private ProvinceModel province;
}
