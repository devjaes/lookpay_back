package dev.jeep.Lookpay.repository;

import org.springframework.stereotype.Repository;

import dev.jeep.Lookpay.models.ClientModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ClientRepository extends JpaRepository<ClientModel, Long> {
    @Query(value = "SELECT * FROM clients WHERE dni=:dni", nativeQuery = true)
    ClientModel getByDni(@Param("dni") String dni);

}
