package dev.jeep.Lookpay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.jeep.Lookpay.models.PaymentModel;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel, Long> {
    @Query(value = "SELECT * FROM payment WHERE company_ruc = :companyRuc", nativeQuery = true)
    List<PaymentModel> findByCompanyRuc(@Param("companyRuc") String companyRuc);

    @Query(value = "SELECT * FROM payment WHERE clientdni=:clientDNI", nativeQuery = true)
    List<PaymentModel> findByClientDNI(@Param("clientDNI") String clientDNI);
}
