package dev.jeep.Lookpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.jeep.Lookpay.models.CompanyModel;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyModel, Long> {
    @Query(value = "SELECT * FROM companies WHERE ruc = :ruc", nativeQuery = true)
    CompanyModel getByRuc(@Param("ruc") String ruc);
}
