package dev.jeep.Lookpay.controllers;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jeep.Lookpay.services.CityService;

@RestController
@RequestMapping("/city") // http://localhost:8080/user
public class CityController {
    @Autowired
    private CityService cityService;

    @GetMapping()
    public ResponseEntity<LinkedHashMap<String, Object>> getCities() {
        return cityService.getCities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> getCity(@Param("id") Long id) {
        return cityService.get(id);
    }

    @GetMapping("/provinces")
    public ResponseEntity<LinkedHashMap<String, Object>> getProvinces() {
        return cityService.getProvinces();
    }

    @GetMapping("/provinces/{id}")
    public ResponseEntity<LinkedHashMap<String, Object>> getCitiesByProvinceId(@PathVariable("id") Long id) {
        return cityService.getCitiesByProvinceId(id);
    }

}
