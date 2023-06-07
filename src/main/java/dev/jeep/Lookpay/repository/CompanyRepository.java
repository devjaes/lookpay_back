package dev.jeep.Lookpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.jeep.Lookpay.models.CompanyModel;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyModel, Long> {

}
