package dev.jeep.Lookpay.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.jeep.Lookpay.dtos.BankCoopAccountResponseDTO;
import dev.jeep.Lookpay.enums.BankAccountTypeEnum;
import dev.jeep.Lookpay.enums.BankCoopEnum;
import dev.jeep.Lookpay.models.BankCoopAccountModel;
import dev.jeep.Lookpay.models.ClientModel;
import dev.jeep.Lookpay.models.CompanyModel;
import dev.jeep.Lookpay.models.PaymentMethodModel;
import dev.jeep.Lookpay.repository.BankCoopAccountRepository;
import dev.jeep.Lookpay.repository.ClientRepository;
import dev.jeep.Lookpay.repository.CompanyRepository;
import dev.jeep.Lookpay.repository.PaymentMethodRepository;

@Service
public class BankAccountService {
    @Autowired
    private BankCoopAccountRepository bankCoopAccountRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CompanyRepository companyRepository;

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

            if (account.getName() != null && account.getName() != "")
                paymentMethod.setName(account.getName());

            if (account.getAccountHolderName() != null && account.getAccountHolderName() != "")
                bankAccount.setAccountHolderName(account.getAccountHolderName());

            if (account.getAccountHolderDNI() != null && account.getAccountHolderDNI() != "")
                bankAccount.setAccountHolderDNI(account.getAccountHolderDNI());

            if (account.getAccountHolderEmail() != null && account.getAccountHolderEmail() != "")
                bankAccount.setAccountHolderEmail(account.getAccountHolderEmail());

            if (account.getNumber() != null && account.getNumber() != "")
                bankAccount.setAccountNumber(account.getNumber());

            if (account.getAccountType() != null && account.getAccountType() != "")
                bankAccount.setAccountType(BankAccountTypeEnum.fromString(account.getAccountType()));

            if (account.getBankName() != null && account.getBankName() != "")
                bankAccount.setBankName(BankCoopEnum.fromString(account.getBankName()));

            if (account.getAccountPassword() != null && account.getAccountPassword() != "")
                bankAccount.setAccountPassword(account.getAccountPassword());

            paymentMethodRepository.save(paymentMethod);
            return bankCoopAccountRepository.save(bankAccount);

        } catch (Exception e) {
            System.out.println("=======================" + e.getMessage());
            return null;
        }
    }

    public boolean validateIfExistsAccount(String number) {
        BankCoopAccountModel bankAccount = this.getBankAccountByNumber(number);

        if (bankAccount == null) {
            return false;
        }

        return true;
    }

    public boolean deleteBankAccount(Long id) {
        try {
            BankCoopAccountModel bankAccount = this.getAccountById(id);

            if (bankAccount == null) {
                return false;
            }

            PaymentMethodModel preferedPaymentMethod = this.getAccountById(id).getPaymentMethod()
                    .getClient().getPreferedAccount();

            if (preferedPaymentMethod != null && preferedPaymentMethod.getBankAccount().getId() == id) {
                ClientModel client = preferedPaymentMethod.getClient();
                client.setPreferedAccount(null);

                clientRepository.save(client);
            }

            preferedPaymentMethod = this.getAccountById(id).getPaymentMethod().getCompany()
                    .getPreferedAccount();

            if (preferedPaymentMethod != null && preferedPaymentMethod.getBankAccount().getId() == id) {
                CompanyModel company = preferedPaymentMethod.getCompany();
                company.setPreferedAccount(null);

                companyRepository.save(company);
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
        return bankCoopAccountRepository.findByAccountId(id);
    }

    public BankCoopAccountModel getBankAccountByNumber(String number) {
        return bankCoopAccountRepository.findByNumber(number);
    }
}
