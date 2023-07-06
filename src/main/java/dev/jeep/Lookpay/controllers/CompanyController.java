package dev.jeep.Lookpay.controllers;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jeep.Lookpay.dtos.UserRegisterDTO;
import dev.jeep.Lookpay.dtos.UserUpdateDTO;
import dev.jeep.Lookpay.models.UserModel;
import dev.jeep.Lookpay.repository.CompanyRepository;
import dev.jeep.Lookpay.services.CompanyService;
import dev.jeep.Lookpay.services.UserService;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<LinkedHashMap<String, Object>> redisterCompany(@RequestBody UserRegisterDTO userDto) {
        return companyService.register(userDto);
    }

    @GetMapping("/all")
    public ResponseEntity<LinkedHashMap<String, Object>> getAll() {
        return companyService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> get(@PathVariable("id") Long id) {
        return companyService.get(id);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> update(@PathVariable("id") Long id,
            @RequestBody UserUpdateDTO user) {
        try {
            UserModel userModel = companyRepository.findById(id).get().getUser();

            if (userModel == null) {
                LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                response.put("message", "User not found");

                return new ResponseEntity<>(response, org.springframework.http.HttpStatus.NOT_FOUND);
            }

            return userService.update(userModel.getId(), user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> getByUserId(@PathVariable("id") Long id) {
        return companyService.getByUserId(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> delete(@PathVariable("id") Long id) {
        return companyService.delete(id);
    }

    @GetMapping("/preferedaccount/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> getPreferedPayment(@PathVariable("id") Long id) {
        return companyService.getPreferedPayment(id);
    }

    @PutMapping(value = "/preferedaccount/{companyId}/{paymentId}")
    public ResponseEntity<LinkedHashMap<String, Object>> setPreferedPayment(@PathVariable("companyId") Long companyId,
            @PathVariable("paymentId") Long id) {
        return companyService.setPreferedPayment(companyId, id);
    }
}
