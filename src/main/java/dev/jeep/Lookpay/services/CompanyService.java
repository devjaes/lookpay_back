package dev.jeep.Lookpay.services;

import java.sql.Timestamp;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.jeep.Lookpay.dtos.UserRegisterDTO;
import dev.jeep.Lookpay.models.CompanyModel;
import dev.jeep.Lookpay.models.UserModel;
import dev.jeep.Lookpay.repository.CompanyRepository;

@Service
public class CompanyService {
    @Autowired
    private UserService userService;

    @Autowired
    private CompanyRepository companyRepository;

    public ResponseEntity<LinkedHashMap<String, Object>> register(UserRegisterDTO userDto, String ruc,
            String originDate) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        if (this.validateIfExists(ruc)) {
            response.put("message", "Company already exist");
            response.put("status", HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<LinkedHashMap<String, Object>> userResponse = userService.register(userDto);

        if (userResponse.getStatusCode().value() != HttpStatus.CREATED.value()) {
            return userResponse;
        }

        UserModel user = userService.convertDtoToModel(userDto);

        CompanyModel newCompany = new CompanyModel(null, ruc, Timestamp.valueOf(originDate), user, null);
        companyRepository.save(newCompany);

        response.put("message", "Company created successfully");
        response.put("status", HttpStatus.CREATED.value());
        response.put("company", newCompany);

        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.CREATED);

    }

    public boolean validateIfExists(String ruc) {
        CompanyModel company = companyRepository.getByRuc(ruc);
        return company != null;
    }
}
