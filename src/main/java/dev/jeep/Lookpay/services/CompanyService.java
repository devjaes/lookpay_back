package dev.jeep.Lookpay.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.jeep.Lookpay.dtos.CompanyResponseDTO;
import dev.jeep.Lookpay.dtos.UserRegisterDTO;
import dev.jeep.Lookpay.enums.RolEnum;
import dev.jeep.Lookpay.models.CompanyModel;
import dev.jeep.Lookpay.models.UserModel;
import dev.jeep.Lookpay.repository.CompanyRepository;
import dev.jeep.Lookpay.utils.DateUtil;

@Service
public class CompanyService {
    @Autowired
    private UserService userService;

    @Autowired
    private CompanyRepository companyRepository;

    public ResponseEntity<LinkedHashMap<String, Object>> register(UserRegisterDTO userDto) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        if (this.validateIfExists(userDto.getDni_ruc())) {
            response.put("message", "Company already exist");
            response.put("status", HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<LinkedHashMap<String, Object>> userResponse = userService.register(userDto);

        if (userResponse.getStatusCode().value() != HttpStatus.CREATED.value()) {
            return userResponse;
        }

        UserModel user = userService.getByEmail(userDto.getEmail());

        CompanyModel newCompany = new CompanyModel(null, userDto.getDni_ruc(),
                Timestamp.valueOf(DateUtil.transformWebDateToDBDate(userDto.getOriginDate())), user, null);
        companyRepository.save(newCompany);

        CompanyResponseDTO companyDto = this.convertModelToDto(newCompany);

        response.put("message", "Company created successfully");
        response.put("status", HttpStatus.CREATED.value());
        response.put("company", companyDto);

        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.CREATED);

    }

    public boolean validateIfExists(String ruc) {
        CompanyModel company = companyRepository.getByRuc(ruc);
        return company != null;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getCompanyById(String rol, Long companyId) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        if (!RolEnum.getRolEnum(rol).equals(RolEnum.ADMIN)) {
            response.put("message", "You don't have permission to do this");
            response.put("status", HttpStatus.UNAUTHORIZED.value());

            return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.UNAUTHORIZED);
        }

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

}
