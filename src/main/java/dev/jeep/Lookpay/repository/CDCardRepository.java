package dev.jeep.Lookpay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.jeep.Lookpay.models.CDCardModel;

@Repository
public interface CDCardRepository extends JpaRepository<CDCardModel, Long> {
    @Query(value = "SELECT * FROM cd_cards WHERE number = :number", nativeQuery = true)
    CDCardModel findByNumber(@Param("number") String number);

    @Query(value = "SELECT cd.id, cd.balance, cd.card_holder_name, cd.card_type, cd.cvv, cd.expiration_date, cd.\"number\" FROM cd_cards cd inner join payment_methods p on p.cd_card_id = cd.id WHERE p.client_id =:clientId", nativeQuery = true)
    List<CDCardModel> findAllByClientId(@Param("clientId") Long clientId);

    @Query(value = "SELECT cd.id, cd.balance, cd.card_holder_name, cd.card_type, cd.cvv, cd.expiration_date, cd.\"number\" FROM cd_cards cd inner join payment_methods p on p.cd_card_id = cd.id WHERE p.client_id =:clientId and cd.card_type = 'CREDIT_CARD'", nativeQuery = true)
    List<CDCardModel> findCDCardsByClientId(@Param("clientId") Long clientId);

    @Query(value = "SELECT cd.id, cd.balance, cd.card_holder_name, cd.card_type, cd.cvv, cd.expiration_date, cd.\"number\" FROM cd_cards cd inner join payment_methods p on p.cd_card_id = cd.id WHERE p.client_id =:clientId and cd.card_type = 'DEBIT_CARD'", nativeQuery = true)
    List<CDCardModel> findDDCardsByClientId(@Param("clientId") Long clientId);

}
