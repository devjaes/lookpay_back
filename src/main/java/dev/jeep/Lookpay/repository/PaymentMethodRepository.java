package dev.jeep.Lookpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.jeep.Lookpay.models.PaymentMethodModel;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethodModel, Long> {

}
