package dev.jeep.Lookpay.services;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.jeep.Lookpay.dtos.PaymentCreationDTO;
import dev.jeep.Lookpay.models.BankCoopAccountModel;
import dev.jeep.Lookpay.models.CDCardModel;
import dev.jeep.Lookpay.models.ClientModel;
import dev.jeep.Lookpay.models.PaymentMethodModel;
import dev.jeep.Lookpay.models.PaymentModel;
import dev.jeep.Lookpay.repository.PaymentRepository;

@Service
public class PaymentService {
    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private PaymentRepository paymentRepository;

    public ResponseEntity<LinkedHashMap<String, Object>> responseCreatePayment(PaymentCreationDTO payment) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {
            PaymentModel newPayment = createPayment(payment);

            if (newPayment == null) {
                response.put("message", "Error al crear el pago");
                response.put("status", 400);
                response.put("error", "error aqui");
                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
            }

            response.put("message", "Pago creado correctamente");
            response.put("status", 201);
            response.put("payment", newPayment);
            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            response.put("message", "Error al crear el pago");
            response.put("status", 400);
            response.put("error", e.getMessage());
            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

    }

    public PaymentModel createPayment(PaymentCreationDTO payment) {
        try {
            ClientModel client = clientService.getByDni(payment.getClientDNI());
            BankCoopAccountModel companyBankAccount = paymentMethodService.getBankAccountByNumber(
                    payment.getCompanyAccountNumber());

            if (companyBankAccount == null) {
                System.out.println("companyBankAccount is null");
                return null;
            }

            if (client == null) {
                return null;
            }

            PaymentMethodModel preferedAccount = client.getPreferedAccount();

            if (preferedAccount == null
                    || preferedAccount.getCdCard() == null && preferedAccount.getBankAccount() == null
                    || preferedAccount.getCdCard() != null
                            && preferedAccount.getCdCard().getBalance() < payment.getAmount()
                    || preferedAccount.getBankAccount() != null
                            && preferedAccount.getBankAccount().getBalance() < payment.getAmount()) {
                List<PaymentMethodModel> accounts = client.getPaymentMethods();

                if (accounts.size() == 0) {
                    return null;
                }

                for (PaymentMethodModel account : accounts) {
                    if (account.getCdCard() != null) {
                        CDCardModel card = account.getCdCard();

                        if (card.getBalance() >= payment.getAmount()) {
                            card.setBalance(card.getBalance() - payment.getAmount());

                            PaymentModel newPayment = new PaymentModel();
                            newPayment.setName(payment.getName());
                            newPayment.setAmount(payment.getAmount());
                            newPayment.setDescription(payment.getDescription());
                            newPayment.setPaymentDate(Timestamp.valueOf(payment.getPaymentDate().replaceAll("T", " ")));
                            newPayment.setCompanyName(payment.getCompanyName());
                            newPayment.setCompanyRuc(payment.getCompanyRuc());
                            newPayment.setCompanyAccountName(payment.getCompanyAccountName());
                            newPayment.setCompanyAccountNumber(payment.getCompanyAccountNumber());
                            newPayment.setClientName(client.getUser().getName());
                            newPayment.setClientDNI(payment.getClientDNI());
                            newPayment.setClientAccountName(account.getName());
                            newPayment.setClientAccountNumber(card.getNumber());

                            companyBankAccount.setBalance(companyBankAccount.getBalance() + payment.getAmount());

                            paymentMethodService.updateBankAccount(companyBankAccount);
                            paymentMethodService.updateCard(card);

                            return paymentRepository.save(newPayment);
                        }

                    } else if (account.getBankAccount() != null) {
                        BankCoopAccountModel bankAccount = account.getBankAccount();

                        if (bankAccount.getBalance() >= payment.getAmount()) {
                            bankAccount.setBalance(bankAccount.getBalance() - payment.getAmount());

                            PaymentModel newPayment = new PaymentModel();
                            newPayment.setName(payment.getName());
                            newPayment.setAmount(payment.getAmount());
                            newPayment.setDescription(payment.getDescription());
                            newPayment.setPaymentDate(Timestamp.valueOf(payment.getPaymentDate().replaceAll("T", " ")));
                            newPayment.setCompanyName(payment.getCompanyName());
                            newPayment.setCompanyRuc(payment.getCompanyRuc());
                            newPayment.setCompanyAccountName(payment.getCompanyAccountName());
                            newPayment.setCompanyAccountNumber(payment.getCompanyAccountNumber());
                            newPayment.setClientName(client.getUser().getName());
                            newPayment.setClientDNI(payment.getClientDNI());
                            newPayment.setClientAccountName(account.getName());
                            newPayment.setClientAccountNumber(bankAccount.getAccountNumber());

                            companyBankAccount.setBalance(companyBankAccount.getBalance() + payment.getAmount());

                            paymentMethodService.updateBankAccount(companyBankAccount);

                            paymentMethodService.updateBankAccount(bankAccount);

                            return paymentRepository.save(newPayment);
                        }
                    }
                }

                return null;

            } else {
                if (preferedAccount.getCdCard() != null) {
                    CDCardModel card = preferedAccount.getCdCard();

                    if (card.getBalance() >= payment.getAmount()) {
                        card.setBalance(card.getBalance() - payment.getAmount());

                        PaymentModel newPayment = new PaymentModel();
                        newPayment.setName(payment.getName());
                        newPayment.setAmount(payment.getAmount());
                        newPayment.setDescription(payment.getDescription());
                        newPayment.setPaymentDate(Timestamp.valueOf(payment.getPaymentDate().replaceAll("T", " ")));
                        newPayment.setCompanyName(payment.getCompanyName());
                        newPayment.setCompanyRuc(payment.getCompanyRuc());
                        newPayment.setCompanyAccountName(payment.getCompanyAccountName());
                        newPayment.setCompanyAccountNumber(payment.getCompanyAccountNumber());
                        newPayment.setClientName(client.getUser().getName());
                        newPayment.setClientDNI(payment.getClientDNI());
                        newPayment.setClientAccountName(preferedAccount.getName());
                        newPayment.setClientAccountNumber(card.getNumber());

                        companyBankAccount.setBalance(companyBankAccount.getBalance() + payment.getAmount());

                        paymentMethodService.updateBankAccount(companyBankAccount);

                        paymentMethodService.updateCard(card);

                        return paymentRepository.save(newPayment);
                    }

                }

                if (preferedAccount.getBankAccount() != null) {
                    BankCoopAccountModel bankAccount = preferedAccount.getBankAccount();

                    if (bankAccount.getBalance() >= payment.getAmount()) {
                        bankAccount.setBalance(bankAccount.getBalance() - payment.getAmount());

                        PaymentModel newPayment = new PaymentModel();
                        newPayment.setName(payment.getName());
                        newPayment.setAmount(payment.getAmount());
                        newPayment.setDescription(payment.getDescription());
                        newPayment.setPaymentDate(Timestamp.valueOf(payment.getPaymentDate().replaceAll("T", " ")));
                        newPayment.setCompanyName(payment.getCompanyName());
                        newPayment.setCompanyRuc(payment.getCompanyRuc());
                        newPayment.setCompanyAccountName(payment.getCompanyAccountName());
                        newPayment.setCompanyAccountNumber(payment.getCompanyAccountNumber());
                        newPayment.setClientName(client.getUser().getName());
                        newPayment.setClientDNI(payment.getClientDNI());
                        newPayment.setClientAccountName(preferedAccount.getName());
                        newPayment.setClientAccountNumber(bankAccount.getAccountNumber());

                        paymentMethodService.updateBankAccount(bankAccount);

                        companyBankAccount.setBalance(companyBankAccount.getBalance() + payment.getAmount());

                        paymentMethodService.updateBankAccount(companyBankAccount);

                        return paymentRepository.save(newPayment);
                    }

                    return null;
                }

                return null;
            }

        } catch (Exception e) {
            System.out.println("================================+++========================" + e.getMessage());
            return null;
        }
    }
}
