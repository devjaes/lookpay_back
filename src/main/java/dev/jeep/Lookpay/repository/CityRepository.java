package dev.jeep.Lookpay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.jeep.Lookpay.models.CityModel;

@Repository
public interface CityRepository extends JpaRepository<CityModel, Long> {

    @Query(value = "SELECT * FROM cities WHERE province_id=:provinceId", nativeQuery = true)
    List<CityModel> getByProvinceId(@Param("provinceId") Long provinceId);
}
