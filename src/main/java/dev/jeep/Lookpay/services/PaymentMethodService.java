package dev.jeep.Lookpay.services;

import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.jeep.Lookpay.dtos.BankCoopAccountResponseDTO;
import dev.jeep.Lookpay.dtos.CardResponseDTO;
import dev.jeep.Lookpay.dtos.PaymentMethodRegisterDTO;
import dev.jeep.Lookpay.enums.BankAccountTypeEnum;
import dev.jeep.Lookpay.enums.BankCoopEnum;
import dev.jeep.Lookpay.enums.CardTypeEnum;
import dev.jeep.Lookpay.models.BankCoopAccountModel;
import dev.jeep.Lookpay.models.CDCardModel;
import dev.jeep.Lookpay.models.ClientModel;
import dev.jeep.Lookpay.models.CompanyModel;
import dev.jeep.Lookpay.models.PaymentMethodModel;
import dev.jeep.Lookpay.repository.BankCoopAccountRepository;
import dev.jeep.Lookpay.repository.CDCardRepository;
import dev.jeep.Lookpay.repository.ClientRepository;
import dev.jeep.Lookpay.repository.CompanyRepository;
import dev.jeep.Lookpay.repository.PaymentMethodRepository;

@Service
public class PaymentMethodService {
    @Autowired
    private ClientService clientService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private CDCardRepository cdCardRepository;

    @Autowired
    private BankCoopAccountRepository bankCoopAccountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public ResponseEntity<LinkedHashMap<String, Object>> register(PaymentMethodRegisterDTO paymentMethodDto) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {
            if (paymentMethodDto.isCard()) {
                if (cdCardRepository.findByNumber(paymentMethodDto.getNumber()) != null) {
                    response.put("message", "Card already exist");
                    response.put("status", HttpStatus.BAD_REQUEST.value());

                    return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
                }
            } else {
                if (bankCoopAccountRepository.findByNumber(paymentMethodDto.getNumber()) != null) {
                    response.put("message", "Account already exist");
                    response.put("status", HttpStatus.BAD_REQUEST.value());

                    return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
                }
            }

            if (paymentMethodDto.isClient()) {
                if (!clientService.validateIfExists(paymentMethodDto.getUserDNI())) {
                    response.put("message", "Client not found");
                    response.put("status", HttpStatus.BAD_REQUEST.value());

                    return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
                }

                ClientModel client = clientService.getByDni(paymentMethodDto.getUserDNI());

                if (paymentMethodDto.isCard()) {

                    CDCardModel newCard = new CDCardModel(null, CardTypeEnum.fromString(paymentMethodDto.getType()),
                            paymentMethodDto.getNumber(), paymentMethodDto.getHolderName(), paymentMethodDto.getCode(),
                            paymentMethodDto.getExpirationDate(), 450.00, null);

                    CDCardModel createdCard = cdCardRepository.save(newCard);

                    PaymentMethodModel paymentMethod = new PaymentMethodModel(null, paymentMethodDto.getName(), null,
                            createdCard, null, null, null, client);

                    PaymentMethodModel createdPMethod = paymentMethodRepository.save(paymentMethod);

                    client.getPaymentMethods().add(createdPMethod);
                    clientRepository.save(client);

                    CardResponseDTO cardResponse = new CardResponseDTO(createdPMethod.getCdCard().getId(),
                            createdPMethod.getName(), createdPMethod.getCdCard().getNumber(),
                            createdPMethod.getCdCard().getCardType().toString(),
                            createdPMethod.getCdCard().getCardHolderName(), createdPMethod.getCdCard().getCcv(),
                            createdPMethod.getCdCard().getExpirationDate().toString());

                    response.put("message", "Card created successfully");

                    response.put("status", HttpStatus.CREATED.value());
                    response.put("card", cardResponse);

                    return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.CREATED);
                } else {
                    BankCoopAccountModel newAccount = new BankCoopAccountModel(null, paymentMethodDto.getNumber(),
                            BankAccountTypeEnum.fromString(paymentMethodDto.getType()),
                            BankCoopEnum.fromString(paymentMethodDto.getBankName()), paymentMethodDto.getHolderName(),
                            paymentMethodDto.getAccountHolderDNI(), paymentMethodDto.getAccountHolderEmail(),
                            paymentMethodDto.getCode(), 450.00, null);

                    BankCoopAccountModel createdAccount = bankCoopAccountRepository.save(newAccount);

                    PaymentMethodModel paymentMethod = new PaymentMethodModel(null, paymentMethodDto.getName(),
                            createdAccount, null, null, null, null, client);

                    client.getPaymentMethods().add(paymentMethod);
                    clientRepository.save(client);

                    PaymentMethodModel createdPMethod = paymentMethodRepository.save(paymentMethod);

                    BankCoopAccountResponseDTO accountResponse = new BankCoopAccountResponseDTO();
                    accountResponse.setId(createdPMethod.getBankAccount().getId());
                    accountResponse.setName(createdPMethod.getName());
                    accountResponse.setNumber(createdPMethod.getBankAccount().getAccountNumber());
                    accountResponse.setBankName(createdPMethod.getBankAccount().getBankName().toString());
                    accountResponse.setAccountType(createdPMethod.getBankAccount().getAccountType().toString());
                    accountResponse.setAccountHolderName(createdPMethod.getBankAccount().getAccountHolderName());
                    accountResponse.setAccountHolderDNI(createdPMethod.getBankAccount().getAccountHolderDNI());
                    accountResponse.setAccountHolderEmail(createdPMethod.getBankAccount().getAccountHolderEmail());
                    accountResponse.setAccountPassword(createdPMethod.getBankAccount().getAccountPassword());

                    response.put("message", "Account created successfully");
                    response.put("status", HttpStatus.CREATED.value());
                    response.put("account", accountResponse);

                    return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.CREATED);
                }
            } else {
                if (!companyService.validateIfExists(paymentMethodDto.getUserDNI())) {
                    response.put("message", "Company not found");
                    response.put("status", HttpStatus.BAD_REQUEST.value());

                    return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
                }

                CompanyModel company = companyService.getByRuc(paymentMethodDto.getUserDNI());

                if (company == null) {
                    response.put("message", "Company not found");
                    response.put("status", HttpStatus.BAD_REQUEST.value());
                    System.out.println(company);

                    return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
                }

                BankCoopAccountModel newAccount = new BankCoopAccountModel(null, paymentMethodDto.getNumber(),
                        BankAccountTypeEnum.fromString(paymentMethodDto.getType()),
                        BankCoopEnum.fromString(paymentMethodDto.getBankName()), paymentMethodDto.getHolderName(),
                        paymentMethodDto.getAccountHolderDNI(), paymentMethodDto.getAccountHolderEmail(),
                        paymentMethodDto.getCode(), 450.00, null);

                BankCoopAccountModel createdAccount = bankCoopAccountRepository.save(newAccount);

                PaymentMethodModel paymentMethod = new PaymentMethodModel(null, paymentMethodDto.getName(),
                        createdAccount, null, null, null, company, null);

                company.getPaymentMethods().add(paymentMethod);
                companyRepository.save(company);

                PaymentMethodModel createdPMethod = paymentMethodRepository.save(paymentMethod);

                BankCoopAccountResponseDTO accountResponse = new BankCoopAccountResponseDTO();
                accountResponse.setId(createdPMethod.getBankAccount().getId());
                accountResponse.setName(createdPMethod.getName());
                accountResponse.setNumber(createdPMethod.getBankAccount().getAccountNumber());
                accountResponse.setBankName(createdPMethod.getBankAccount().getBankName().toString());
                accountResponse.setAccountType(createdPMethod.getBankAccount().getAccountType().toString());
                accountResponse.setAccountHolderName(createdPMethod.getBankAccount().getAccountHolderName());
                accountResponse.setAccountHolderDNI(createdPMethod.getBankAccount().getAccountHolderDNI());
                accountResponse.setAccountHolderEmail(createdPMethod.getBankAccount().getAccountHolderEmail());
                accountResponse.setAccountPassword(createdPMethod.getBankAccount().getAccountPassword());

                response.put("message", "Account created successfully");
                response.put("company", company.getRuc());
                response.put("company2", paymentMethod.getCompanyId().getRuc());

                response.put("status", HttpStatus.CREATED.value());
                response.put("account", accountResponse);

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            response.put("message", "Error creating payment method");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    public ResponseEntity<LinkedHashMap<String, Object>> responseUpdateCard(CardResponseDTO card) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {
            CDCardModel cdCard = this.getCardById(card.getId());

            if (cdCard == null) {
                response.put("message", "Card not found");
                response.put("status", HttpStatus.BAD_REQUEST.value());
            }

            CDCardModel updateCard = this.updateCard(card);

            if (updateCard == null) {
                response.put("message", "Error updating card");
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            CardResponseDTO cardResponse = new CardResponseDTO();
            cardResponse.setId(updateCard.getId());
            cardResponse.setName(updateCard.getPaymentMethod().getName());
            cardResponse.setCardHolderName(updateCard.getCardHolderName());
            cardResponse.setCardType(updateCard.getCardType().toString());
            cardResponse.setCcv(updateCard.getCcv());
            cardResponse.setExpirationDate(updateCard.getExpirationDate().toString());
            cardResponse.setNumber(updateCard.getNumber());

            response.put("message", "Card updated successfully");
            response.put("status", HttpStatus.OK.value());
            response.put("card", cardResponse);

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("message", "Error updating card");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> responseUpdateBankAccount(BankCoopAccountResponseDTO account) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {
            BankCoopAccountModel bankAccount = this.getAccountById(account.getId());

            if (bankAccount == null) {
                response.put("message", "Bank account not found");
                response.put("status", HttpStatus.BAD_REQUEST.value());

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
            }

            BankCoopAccountModel updateAccount = this.updateBankAccount(account);

            if (updateAccount == null) {
                response.put("message", "Error updating bank account");
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            BankCoopAccountResponseDTO accountResponse = new BankCoopAccountResponseDTO();
            accountResponse.setId(updateAccount.getId());
            accountResponse.setName(updateAccount.getBankName().toString());
            accountResponse.setNumber(updateAccount.getAccountNumber());
            accountResponse.setBankName(updateAccount.getBankName().toString());
            accountResponse.setAccountType(updateAccount.getAccountType().toString());
            accountResponse.setAccountHolderName(updateAccount.getAccountHolderName());
            accountResponse.setAccountHolderDNI(updateAccount.getAccountHolderDNI());
            accountResponse.setAccountHolderEmail(updateAccount.getAccountHolderEmail());
            accountResponse.setAccountPassword(updateAccount.getAccountPassword());

            response.put("message", "Bank account updated successfully");
            response.put("status", HttpStatus.OK.value());
            response.put("account", accountResponse);

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("message", "Error updating bank account");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<CDCardModel> getAllUserCards(Long clientId) {
        return cdCardRepository.findAllByClientId(clientId);
    }

    public CDCardModel updateCard(CDCardModel card) {
        return cdCardRepository.save(card);
    }

    public CDCardModel updateCard(CardResponseDTO card) {
        try {
            CDCardModel cdCard = this.getCardById(card.getId());

            if (cdCard == null) {
                return null;
            }

            PaymentMethodModel paymentMethod = cdCard.getPaymentMethod();

            if (card.getName() != "")
                paymentMethod.setName(card.getName());

            if (card.getCardHolderName() != "")
                cdCard.setCardHolderName(card.getCardHolderName());

            if (card.getCardType() != "")
                cdCard.setCardType(CardTypeEnum.fromString(card.getCardType()));

            if (card.getCcv() != "")
                cdCard.setCcv(card.getCcv());

            if (card.getExpirationDate() != "")
                cdCard.setExpirationDate(card.getExpirationDate());

            if (card.getNumber() != "")
                cdCard.setNumber(card.getNumber());

            paymentMethodRepository.save(paymentMethod);
            return cdCardRepository.save(cdCard);

        } catch (Exception e) {
            System.out.println("=======================" + e.getMessage());
            return null;
        }
    }

    public boolean deleteCard(Long id) {
        try {
            CDCardModel card = this.getCardById(id);

            if (card == null) {
                return false;
            }

            PaymentMethodModel paymentMethod = card.getPaymentMethod();
            paymentMethodRepository.delete(paymentMethod);
            cdCardRepository.delete(card);

            return true;

        } catch (Exception e) {
            return false;
        }
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

    public List<PaymentMethodModel> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }

    public List<CDCardModel> getAllCCards(Long clientId) {
        return cdCardRepository.findCDCardsByClientId(clientId);
    }

    public CDCardModel getCardById(Long id) {
        return cdCardRepository.findById(id).orElse(null);
    }

    public BankCoopAccountModel getAccountById(Long id) {
        return bankCoopAccountRepository.findById(id).orElse(null);
    }

    public BankCoopAccountModel getBankAccountByNumber(String number) {
        return bankCoopAccountRepository.findByNumber(number);
    }

    public PaymentMethodModel getPaymentMethodById(Long id) {
        return paymentMethodRepository.findById(id).orElse(null);
    }

}
