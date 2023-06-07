package dev.jeep.Lookpay.repository;

import org.springframework.stereotype.Repository;

import dev.jeep.Lookpay.models.ClientModel;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ClientRepository extends JpaRepository<ClientModel, Long> {

}
