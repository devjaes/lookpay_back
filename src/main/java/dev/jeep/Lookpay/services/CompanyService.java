package dev.jeep.Lookpay.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.jeep.Lookpay.dtos.BankCoopAccountResponseDTO;
import dev.jeep.Lookpay.dtos.CompanyResponseDTO;
import dev.jeep.Lookpay.dtos.UserRegisterDTO;
import dev.jeep.Lookpay.models.BankCoopAccountModel;
import dev.jeep.Lookpay.models.CompanyModel;
import dev.jeep.Lookpay.models.PaymentMethodModel;
import dev.jeep.Lookpay.models.UserModel;
import dev.jeep.Lookpay.repository.CompanyRepository;
import dev.jeep.Lookpay.repository.UserRepository;

@Service
public class CompanyService {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BankAccountService bankCoopAccountService;

    public ResponseEntity<LinkedHashMap<String, Object>> register(UserRegisterDTO userDto) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {
            if (this.validateIfExists(userDto.getDni_ruc())) {
                response.put("message", "Company already exist");
                response.put("status", HttpStatus.BAD_REQUEST.value());

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
            }

            CompanyModel newCompany = new CompanyModel(null, userDto.getDni_ruc(),
                    Date.valueOf(userDto.getOriginDate()), null, null, null);

            ResponseEntity<LinkedHashMap<String, Object>> userResponse = userService.register(userDto);

            if (userResponse.getStatusCode().value() != HttpStatus.CREATED.value()) {
                response.put("message", "Error creating company");
                response.put("userResponse", userResponse);
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            UserModel user = userService.getByEmail(userDto.getEmail());
            newCompany.setUser(user);
            companyRepository.save(newCompany);

            CompanyResponseDTO companyDto = this.convertModelToDto(newCompany);

            response.put("message", "Company created successfully");
            response.put("status", HttpStatus.CREATED.value());
            response.put("company", companyDto);

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            response.put("message", "Error creating company");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public boolean validateIfExists(String ruc) {
        CompanyModel company = companyRepository.getByRuc(ruc);
        return company != null;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> get(Long companyId) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        CompanyModel company = companyRepository.findById(companyId).get();

        if (company == null) {
            response.put("message", "Company not found");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        CompanyResponseDTO companyDto = this.convertModelToDto(company);

        response.put("message", "Company found");
        response.put("status", HttpStatus.OK.value());
        response.put("company", companyDto);

        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getByUserId(Long userId) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        System.out.println("______________________________________________________________" + userId);
        UserModel userModel = userRepository.findById(userId).get();
        CompanyModel company = userModel.getCompany();

        if (company == null) {
            response.put("message", "Company not found");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        CompanyResponseDTO companyDto = this.convertModelToDto(company);

        response.put("message", "Company found");
        response.put("status", HttpStatus.OK.value());
        response.put("company", companyDto);

        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
    }

    private CompanyResponseDTO convertModelToDto(CompanyModel company) {
        CompanyResponseDTO companyDto = new CompanyResponseDTO(company.getId(), company.getUser().getName(),
                company.getRuc(), company.getUser().getEmail(), company.getUser().getPhoneNumber(),
                company.getUser().getCity().getName(), company.getUser().getCity().getProvince().getName(),
                company.getUser().getAddress());

        return companyDto;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getAll() {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        Iterable<CompanyModel> companies = companyRepository.findAll();

        if (companies == null) {
            response.put("message", "Companys not found");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        ArrayList<CompanyResponseDTO> companiesDto = new ArrayList<>();

        for (CompanyModel company : companies) {
            companiesDto.add(this.convertModelToDto(company));
        }

        response.put("message", "Companies found");
        response.put("status", HttpStatus.OK.value());
        response.put("companies", companiesDto);

        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
    }

    public ResponseEntity<LinkedHashMap<String, Object>> delete(Long id) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        CompanyModel company = companyRepository.findById(id).get();

        if (company == null) {
            response.put("message", "Company not found");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {

            List<BankCoopAccountModel> accounts = bankCoopAccountService.getAllBankAccountsByCompanyId(id);

            for (BankCoopAccountModel account : accounts) {
                bankCoopAccountService.deleteBankAccount(account.getId());
            }

            companyRepository.delete(company);

            response.put("message", "Company deleted successfully");
            response.put("status", HttpStatus.OK.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("message", "Error deleting company");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<LinkedHashMap<String, Object>> setPreferedPayment(Long companyId, Long id) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        CompanyModel company = companyRepository.findById(companyId).get();

        if (company == null) {
            response.put("message", "Company not found");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {

            BankCoopAccountModel account = bankCoopAccountService.getAccountById(id);
            PaymentMethodModel paymentMethod = account.getPaymentMethod();

            company.setPreferedAccount(paymentMethod);

            companyRepository.save(company);

            response.put("message", "Prefered payment method set successfully");
            response.put("status", HttpStatus.OK.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error setting prefered payment method");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getPreferedPayment(Long companyId) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        CompanyModel company = companyRepository.findById(companyId).get();

        if (company == null) {
            response.put("message", "Company not found");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            PaymentMethodModel paymentMethod = company.getPreferedAccount();

            if (paymentMethod == null) {
                response.put("message", "Company has no prefered payment method");
                response.put("status", HttpStatus.NOT_FOUND.value());

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
            }

            if (paymentMethod.getBankAccount() != null) {
                BankCoopAccountModel account = paymentMethod.getBankAccount();

                BankCoopAccountResponseDTO accountDto = new BankCoopAccountResponseDTO();
                accountDto.setId(account.getId());
                accountDto.setName(account.getPaymentMethod().getName());
                accountDto.setNumber(account.getAccountNumber());
                accountDto.setAccountType(account.getAccountType().toString());
                accountDto.setBankName(account.getBankName().toString());
                accountDto.setAccountHolderName(account.getAccountHolderName());
                accountDto.setAccountHolderDNI(account.getAccountHolderDNI());
                accountDto.setAccountHolderEmail(account.getAccountHolderEmail());
                accountDto.setAccountPassword(account.getAccountPassword());

                response.put("message", "Prefered payment method found");
                response.put("status", HttpStatus.OK.value());
                response.put("account", accountDto);

                return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
            }

            response.put("message", "Company has no prefered payment method");
            response.put("status", HttpStatus.NOT_FOUND.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            response.put("message", "Error getting prefered payment method for company");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", e.getMessage());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public CompanyModel getByRuc(String ruc) {
        return companyRepository.getByRuc(ruc);
    }

    public CompanyModel getById(Long id) {
        return companyRepository.findById(id).get();
    }

}
