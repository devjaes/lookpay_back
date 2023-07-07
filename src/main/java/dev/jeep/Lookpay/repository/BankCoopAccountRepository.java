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

        @Query(value = "SELECT b.id, b.balance, b.account_holderdni, b.account_holder_name, b.account_holder_email, b.account_type, b.bank_name, b.account_password, b.account_number FROM bank_coop_accounts b inner join payment_methods p on p.bank_account_id = b.id WHERE p.client_id =:clientId", nativeQuery = true)
        List<BankCoopAccountModel> findAllAccountsByClientID(@Param("clientId") Long id);

        @Query(value = "SELECT b.id, b.balance, b.account_holderdni, b.account_holder_name, b.account_holder_email, b.account_type, b.bank_name, b.account_password, b.account_number FROM bank_coop_accounts b inner join payment_methods p on p.bank_account_id = b.id WHERE b.id =:id", nativeQuery = true)
        BankCoopAccountModel findByAccountId(@Param("id") Long accountId);

        @Query(value = "SELECT b.id, b.balance, b.account_holderdni, b.account_holder_name, b.account_holder_email, b.account_type, b.bank_name, b.account_password, b.account_number FROM bank_coop_accounts b inner join payment_methods p on p.bank_account_id = b.id WHERE p.company_id =:companyId", nativeQuery = true)
        List<BankCoopAccountModel> findAllAccountsByCompanyID(@Param("companyId") Long id);

        @Query(value = "SELECT b.id, b.balance, b.account_number, b.account_holderdni,\n" + //
                        "b.account_holder_email, b.account_holder_name, b.account_type,\n" + //
                        "b.bank_name, b.account_password \n" + //
                        "FROM bank_coop_accounts b \n" + //
                        "inner join payment_methods p \n" + //
                        "on p.bank_account_id = b.id \n" + //
                        "WHERE p.client_id  =:clientId and b.account_type  = 'CURRENT_ACCOUNT'", nativeQuery = true)
        List<BankCoopAccountModel> findBCCByClientId(@Param("clientId") Long clientId);

        @Query(value = "SELECT b.id, b.balance, b.account_number, b.account_holderdni,\n" + //
                        "b.account_holder_email, b.account_holder_name, b.account_type,\n" + //
                        "b.bank_name, b.account_password \n" + //
                        "FROM bank_coop_accounts b \n" + //
                        "inner join payment_methods p \n" + //
                        "on p.bank_account_id = b.id \n" + //
                        "WHERE p.client_id  =:clientId and b.account_type  = 'SAVINGS_ACCOUNT'", nativeQuery = true)
        List<BankCoopAccountModel> findBSCByClientId(@Param("clientId") Long clientId);

        @Query(value = "SELECT b.id, b.balance, b.account_number, b.account_holderdni,\n" + //
                        "b.account_holder_email, b.account_holder_name, b.account_type,\n" + //
                        "b.bank_name, b.account_password \n" + //
                        "FROM bank_coop_accounts b \n" + //
                        "inner join payment_methods p \n" + //
                        "on p.bank_account_id = b.id \n" + //
                        "WHERE p.company_id  =:companyId and b.account_type  = 'CURRENT_ACCOUNT'", nativeQuery = true)
        List<BankCoopAccountModel> findBCCByCompanyId(@Param("companyId") Long companyId);

        @Query(value = "SELECT b.id, b.balance, b.account_number, b.account_holderdni,\n" + //
                        "b.account_holder_email, b.account_holder_name, b.account_type,\n" + //
                        "b.bank_name, b.account_password \n" + //
                        "FROM bank_coop_accounts b \n" + //
                        "inner join payment_methods p \n" + //
                        "on p.bank_account_id = b.id \n" + //
                        "WHERE p.company_id  =:companyId and b.account_type  = 'SAVINGS_ACCOUNT'", nativeQuery = true)

        List<BankCoopAccountModel> findBSCByCompanyId(@Param("companyId") Long companyId);
}
