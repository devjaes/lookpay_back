package dev.jeep.Lookpay.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.jeep.Lookpay.dtos.BankCoopAccountResponseDTO;
import dev.jeep.Lookpay.dtos.CardResponseDTO;
import dev.jeep.Lookpay.dtos.PaymentMethodRegisterDTO;
import dev.jeep.Lookpay.dtos.PaymentsMethodsResponseDTO;
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

    @Autowired
    private CardService cardService;

    @Autowired
    private BankAccountService bankAccountService;

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
                            createdPMethod.getCdCard().getCardHolderName(), createdPMethod.getCdCard().getCvv(),
                            createdPMethod.getCdCard().getExpirationDate().toString());

                    response.put("message", "Card created successfully");

                    response.put("status", HttpStatus.CREATED.value());
                    response.put("account", cardResponse);

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
            CDCardModel cdCard = cardService.getCardById(card.getId());

            if (cdCard == null) {
                response.put("message", "Card not found");
                response.put("status", HttpStatus.BAD_REQUEST.value());
            }

            if (cardService.validateIfExistsCard(card.getNumber())) {
                response.put("message", "Card already exists");
                response.put("status", HttpStatus.BAD_REQUEST.value());

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
            }

            CDCardModel updateCard = cardService.updateCard(card);

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
            cardResponse.setCvv(updateCard.getCvv());
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
            BankCoopAccountModel bankAccount = bankAccountService.getAccountById(account.getId());

            if (bankAccount == null) {
                response.put("message", "Bank account not found");
                response.put("status", HttpStatus.BAD_REQUEST.value());

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
            }

            if (bankAccountService.validateIfExistsAccount(account.getNumber())) {
                response.put("message", "Bank account already exists");
                response.put("status", HttpStatus.BAD_REQUEST.value());

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
            }

            BankCoopAccountModel updateAccount = bankAccountService.updateBankAccount(account);

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

    public ResponseEntity<LinkedHashMap<String, Object>> deletePaymentMethod(Long id, String type) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {
            if (type.equals("CARD")) {

                if (cardService.deleteCard(id)) {
                    response.put("message", "Card deleted successfully");
                    response.put("status", HttpStatus.OK.value());

                    return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
                }

                response.put("message", "Error deleting card");
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (type.equals("ACCOUNT")) {
                if (bankAccountService.deleteBankAccount(id)) {
                    response.put("message", "Bank account deleted successfully");
                    response.put("status", HttpStatus.OK.value());

                    return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
                }

                response.put("message", "Error deleting bank account");
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.put("message", "Error deleting payment method");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

            response.put("error", "Invalid type");

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            response.put("message", "Error deleting payment method");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getTypePaymentMethodsByClientId(Long clientID) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        if (clientID == null) {
            response.put("message", "Client ID is null");
            response.put("status", HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            List<CDCardModel> cCards = cardService.getAllCCardsByClientId(clientID);
            List<CDCardModel> dCards = cardService.getAllDCardsByClientId(clientID);
            List<BankCoopAccountModel> cAccounts = bankAccountService.getAllCAccountByClientId(clientID);
            List<BankCoopAccountModel> sAccounts = bankAccountService.getAllSAccountByClientId(clientID);

            List<CardResponseDTO> cCardsDTO = new ArrayList<>();
            List<CardResponseDTO> dCardsDTO = new ArrayList<>();
            List<BankCoopAccountResponseDTO> cAccountsDTO = new ArrayList<>();
            List<BankCoopAccountResponseDTO> sAccountsDTO = new ArrayList<>();

            for (CDCardModel card : cCards) {
                CardResponseDTO CardResponseDTO = new CardResponseDTO();
                CardResponseDTO.setId(card.getId());
                CardResponseDTO.setName(card.getPaymentMethod().getName());
                CardResponseDTO.setNumber(card.getNumber());
                CardResponseDTO.setCardType(card.getCardType().toString());
                CardResponseDTO.setCardHolderName(card.getCardHolderName());
                CardResponseDTO.setCvv(card.getCvv());
                CardResponseDTO.setExpirationDate(card.getExpirationDate());

                cCardsDTO.add(CardResponseDTO);
            }

            for (CDCardModel card : dCards) {
                CardResponseDTO CardResponseDTO = new CardResponseDTO();
                CardResponseDTO.setId(card.getId());
                CardResponseDTO.setName(card.getPaymentMethod().getName());
                CardResponseDTO.setNumber(card.getNumber());
                CardResponseDTO.setCardType(card.getCardType().toString());
                CardResponseDTO.setCardHolderName(card.getCardHolderName());
                CardResponseDTO.setCvv(card.getCvv());
                CardResponseDTO.setExpirationDate(card.getExpirationDate());

                dCardsDTO.add(CardResponseDTO);
            }

            for (BankCoopAccountModel account : cAccounts) {
                BankCoopAccountResponseDTO bankCoopAccountResponseDTO = new BankCoopAccountResponseDTO();
                bankCoopAccountResponseDTO.setId(account.getId());
                bankCoopAccountResponseDTO.setName(account.getPaymentMethod().getName());
                bankCoopAccountResponseDTO.setNumber(account.getAccountNumber());
                bankCoopAccountResponseDTO.setAccountType(account.getAccountType().toString());
                bankCoopAccountResponseDTO.setBankName(account.getBankName().toString());
                bankCoopAccountResponseDTO.setAccountHolderName(account.getAccountHolderName());
                bankCoopAccountResponseDTO.setAccountHolderDNI(account.getAccountHolderDNI());
                bankCoopAccountResponseDTO.setAccountHolderEmail(account.getAccountHolderEmail());
                bankCoopAccountResponseDTO.setAccountPassword(account.getAccountPassword());

                cAccountsDTO.add(bankCoopAccountResponseDTO);
            }

            for (BankCoopAccountModel account : sAccounts) {
                BankCoopAccountResponseDTO bankCoopAccountResponseDTO = new BankCoopAccountResponseDTO();
                bankCoopAccountResponseDTO.setId(account.getId());
                bankCoopAccountResponseDTO.setName(account.getPaymentMethod().getName());
                bankCoopAccountResponseDTO.setNumber(account.getAccountNumber());
                bankCoopAccountResponseDTO.setAccountType(account.getAccountType().toString());
                bankCoopAccountResponseDTO.setBankName(account.getBankName().toString());
                bankCoopAccountResponseDTO.setAccountHolderName(account.getAccountHolderName());
                bankCoopAccountResponseDTO.setAccountHolderDNI(account.getAccountHolderDNI());
                bankCoopAccountResponseDTO.setAccountHolderEmail(account.getAccountHolderEmail());
                bankCoopAccountResponseDTO.setAccountPassword(account.getAccountPassword());

                sAccountsDTO.add(bankCoopAccountResponseDTO);
            }

            PaymentsMethodsResponseDTO paymentMethods = new PaymentsMethodsResponseDTO();

            paymentMethods.setCreditCards(cCardsDTO);
            paymentMethods.setDebitCards(dCardsDTO);
            paymentMethods.setBankCoopCurrentsAccounts(cAccountsDTO);
            paymentMethods.setBankCoopSavingsAccounts(sAccountsDTO);

            response.put("message", "Payment methods found");
            response.put("status", HttpStatus.OK.value());
            response.put("paymentMethods", paymentMethods);

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("message", "Error getting payment methods");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getTypePaymentMethodsByCompanyId(Long companyID) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        if (companyID == null) {
            response.put("message", "Company ID is null");
            response.put("status", HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            List<BankCoopAccountModel> cAccounts = bankAccountService.getAllCAccountByCompanyId(companyID);
            List<BankCoopAccountModel> sAccounts = bankAccountService.getAllSAccountByCompanyId(companyID);

            List<BankCoopAccountResponseDTO> cAccountsDTO = new ArrayList<>();
            List<BankCoopAccountResponseDTO> sAccountsDTO = new ArrayList<>();

            for (BankCoopAccountModel account : cAccounts) {
                BankCoopAccountResponseDTO bankCoopAccountResponseDTO = new BankCoopAccountResponseDTO();
                bankCoopAccountResponseDTO.setId(account.getId());
                bankCoopAccountResponseDTO.setName(account.getPaymentMethod().getName());
                bankCoopAccountResponseDTO.setNumber(account.getAccountNumber());
                bankCoopAccountResponseDTO.setAccountType(account.getAccountType().toString());
                bankCoopAccountResponseDTO.setBankName(account.getBankName().toString());
                bankCoopAccountResponseDTO.setAccountHolderName(account.getAccountHolderName());
                bankCoopAccountResponseDTO.setAccountHolderDNI(account.getAccountHolderDNI());
                bankCoopAccountResponseDTO.setAccountHolderEmail(account.getAccountHolderEmail());
                bankCoopAccountResponseDTO.setAccountPassword(account.getAccountPassword());

                cAccountsDTO.add(bankCoopAccountResponseDTO);
            }

            for (BankCoopAccountModel account : sAccounts) {
                BankCoopAccountResponseDTO bankCoopAccountResponseDTO = new BankCoopAccountResponseDTO();
                bankCoopAccountResponseDTO.setId(account.getId());
                bankCoopAccountResponseDTO.setName(account.getPaymentMethod().getName());
                bankCoopAccountResponseDTO.setNumber(account.getAccountNumber());
                bankCoopAccountResponseDTO.setAccountType(account.getAccountType().toString());
                bankCoopAccountResponseDTO.setBankName(account.getBankName().toString());
                bankCoopAccountResponseDTO.setAccountHolderName(account.getAccountHolderName());
                bankCoopAccountResponseDTO.setAccountHolderDNI(account.getAccountHolderDNI());
                bankCoopAccountResponseDTO.setAccountHolderEmail(account.getAccountHolderEmail());
                bankCoopAccountResponseDTO.setAccountPassword(account.getAccountPassword());

                sAccountsDTO.add(bankCoopAccountResponseDTO);
            }

            PaymentsMethodsResponseDTO paymentMethods = new PaymentsMethodsResponseDTO();

            paymentMethods.setBankCoopCurrentsAccounts(cAccountsDTO);
            paymentMethods.setBankCoopSavingsAccounts(sAccountsDTO);

            response.put("message", "Payment methods found");
            response.put("status", HttpStatus.OK.value());
            response.put("paymentMethods", paymentMethods);

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("message", "Error getting payment methods");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<PaymentMethodModel> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }

    public PaymentMethodModel getPaymentMethodById(Long id) {
        return paymentMethodRepository.findById(id).orElse(null);
    }

}
