package dev.jeep.Lookpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.jeep.Lookpay.models.PaymentModel;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel, Long> {

}
