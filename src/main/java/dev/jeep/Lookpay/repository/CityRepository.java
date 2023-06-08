package dev.jeep.Lookpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.jeep.Lookpay.models.CityModel;

@Repository
public interface CityRepository extends JpaRepository<CityModel, Long> {

}
