package dev.jeep.Lookpay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.jeep.Lookpay.models.BankCoopAccountModel;

@Repository
public interface BankCoopAccountRepository extends JpaRepository<BankCoopAccountModel, Long> {
    @Query(value = "SELECT * FROM bank_coop_accounts WHERE account_number = :number", nativeQuery = true)
    BankCoopAccountModel findByNumber(@Param("number") String number);

    @Query(value = "SELECT cd.id, cd.balance, cd.card_holder_name, cd.card_type, cd.ccv, cd.expiration_date, cd.\"number\" FROM cd_cards cd inner join payment_methods p on p.cd_card_id = cd.id WHERE p.client_id =:clientId and cd.card_type = 'DEBIT_CARD'", nativeQuery = true)
    List<BankCoopAccountModel> findBCCByClientId(@Param("clientId") Long clientId);

    @Query(value = "SELECT cd.id, cd.balance, cd.card_holder_name, cd.card_type, cd.ccv, cd.expiration_date, cd.\"number\" FROM cd_cards cd inner join payment_methods p on p.cd_card_id = cd.id WHERE p.client_id =:clientId and cd.card_type = 'DEBIT_CARD'", nativeQuery = true)
    List<BankCoopAccountModel> findBSCByClientId(@Param("clientId") Long clientId);

    @Query(value = "SELECT cd.id, cd.balance, cd.card_holder_name, cd.card_type, cd.ccv, cd.expiration_date, cd.\"number\" FROM cd_cards cd inner join payment_methods p on p.cd_card_id = cd.id WHERE p.client_id =:clientId and cd.card_type = 'DEBIT_CARD'", nativeQuery = true)
    List<BankCoopAccountModel> findBCCByCompanyId(@Param("clientId") Long clientId);

    @Query(value = "SELECT cd.id, cd.balance, cd.card_holder_name, cd.card_type, cd.ccv, cd.expiration_date, cd.\"number\" FROM cd_cards cd inner join payment_methods p on p.cd_card_id = cd.id WHERE p.client_id =:clientId and cd.card_type = 'DEBIT_CARD'", nativeQuery = true)
    List<BankCoopAccountModel> findBSCByCompanyId(@Param("clientId") Long clientId);
}
