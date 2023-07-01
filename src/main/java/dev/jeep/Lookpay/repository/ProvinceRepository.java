package dev.jeep.Lookpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.jeep.Lookpay.models.ProvinceModel;

@Repository
public interface ProvinceRepository extends JpaRepository<ProvinceModel, Long> {

}
