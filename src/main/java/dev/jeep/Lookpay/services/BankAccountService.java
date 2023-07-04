package dev.jeep.Lookpay.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.jeep.Lookpay.dtos.BankCoopAccountResponseDTO;
import dev.jeep.Lookpay.enums.BankAccountTypeEnum;
import dev.jeep.Lookpay.enums.BankCoopEnum;
import dev.jeep.Lookpay.models.BankCoopAccountModel;
import dev.jeep.Lookpay.models.PaymentMethodModel;
import dev.jeep.Lookpay.repository.BankCoopAccountRepository;
import dev.jeep.Lookpay.repository.PaymentMethodRepository;

@Service
public class BankAccountService {
    @Autowired
    private BankCoopAccountRepository bankCoopAccountRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public List<BankCoopAccountModel> getAllBankAccountsByClientId(Long clientId) {
        return bankCoopAccountRepository.findAllAccountsByClientID(clientId);
    }

    public List<BankCoopAccountModel> getAllBankAccountsByCompanyId(Long companyId) {
        return bankCoopAccountRepository.findAllAccountsByCompanyID(companyId);
    }

    public List<BankCoopAccountModel> getAllCAccountByClientId(Long clientId) {
        return bankCoopAccountRepository.findBCCByClientId(clientId);
    }

    public List<BankCoopAccountModel> getAllSAccountByClientId(Long clientId) {
        return bankCoopAccountRepository.findBSCByClientId(clientId);
    }

    public List<BankCoopAccountModel> getAllCAccountByCompanyId(Long companyId) {
        return bankCoopAccountRepository.findBCCByCompanyId(companyId);
    }

    public List<BankCoopAccountModel> getAllSAccountByCompanyId(Long companyId) {
        return bankCoopAccountRepository.findBSCByCompanyId(companyId);
    }

    public BankCoopAccountModel updateBankAccount(BankCoopAccountModel account) {
        return bankCoopAccountRepository.save(account);
    }

    public BankCoopAccountModel updateBankAccount(BankCoopAccountResponseDTO account) {
        try {
            BankCoopAccountModel bankAccount = this.getAccountById(account.getId());

            if (bankAccount == null) {
                return null;
            }

            PaymentMethodModel paymentMethod = bankAccount.getPaymentMethod();

            if (account.getName() != "")
                paymentMethod.setName(account.getName());

            if (account.getAccountHolderName() != "")
                bankAccount.setAccountHolderName(account.getAccountHolderName());

            if (account.getAccountHolderDNI() != "")
                bankAccount.setAccountHolderDNI(account.getAccountHolderDNI());

            if (account.getAccountHolderEmail() != "")
                bankAccount.setAccountHolderEmail(account.getAccountHolderEmail());

            if (account.getNumber() != "")
                bankAccount.setAccountNumber(account.getNumber());

            if (account.getAccountType() != "")
                bankAccount.setAccountType(BankAccountTypeEnum.fromString(account.getAccountType()));

            if (account.getBankName() != "")
                bankAccount.setBankName(BankCoopEnum.fromString(account.getBankName()));

            if (account.getAccountPassword() != "")
                bankAccount.setAccountPassword(account.getAccountPassword());

            paymentMethodRepository.save(paymentMethod);
            return bankCoopAccountRepository.save(bankAccount);

        } catch (Exception e) {
            System.out.println("=======================" + e.getMessage());
            return null;
        }
    }

    public boolean deleteBankAccount(Long id) {
        try {
            BankCoopAccountModel bankAccount = this.getAccountById(id);

            if (bankAccount == null) {
                return false;
            }

            PaymentMethodModel paymentMethod = bankAccount.getPaymentMethod();
            paymentMethodRepository.delete(paymentMethod);
            bankCoopAccountRepository.delete(bankAccount);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public BankCoopAccountModel getAccountById(Long id) {
        return bankCoopAccountRepository.findById(id).orElse(null);
    }

    public BankCoopAccountModel getBankAccountByNumber(String number) {
        return bankCoopAccountRepository.findByNumber(number);
    }
}
