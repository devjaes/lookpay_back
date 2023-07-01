package dev.jeep.Lookpay.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dev.jeep.Lookpay.dtos.CityDTO;
import dev.jeep.Lookpay.dtos.ProvinceResponseDTO;
import dev.jeep.Lookpay.models.CityModel;
import dev.jeep.Lookpay.models.ProvinceModel;
import dev.jeep.Lookpay.repository.CityRepository;
import dev.jeep.Lookpay.repository.ProvinceRepository;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    public ResponseEntity<LinkedHashMap<String, Object>> getCities() {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        List<CityModel> cities = cityRepository.findAll();
        List<CityDTO> citiesDTO = new ArrayList<CityDTO>();

        for (CityModel city : cities) {
            citiesDTO.add(new CityDTO(city.getId(), city.getName(), city.getProvince().getName()));
        }

        response.put("cities", citiesDTO);
        response.put("status", HttpStatus.OK.value());
        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
    }

    public ResponseEntity<LinkedHashMap<String, Object>> get(Long id) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        CityModel city = cityRepository.findById(id).get();
        CityDTO cityDTO = new CityDTO(city.getId(), city.getName(), city.getProvince().getName());

        response.put("city", cityDTO);
        response.put("status", HttpStatus.OK.value());
        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getProvinces() {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        List<ProvinceModel> provinces = provinceRepository.findAll();
        List<ProvinceResponseDTO> provincesDTO = new ArrayList<ProvinceResponseDTO>();

        for (ProvinceModel province : provinces) {
            provincesDTO.add(new ProvinceResponseDTO(province.getId(), province.getName()));
        }

        response.put("provinces", provincesDTO);
        response.put("status", HttpStatus.OK.value());
        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getCitiesByProvinceId(Long id) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        List<CityModel> cities = cityRepository.getByProvinceId(id);
        List<CityDTO> citiesDTO = new ArrayList<CityDTO>();

        for (CityModel city : cities) {
            citiesDTO.add(new CityDTO(city.getId(), city.getName(), city.getProvince().getName()));
        }

        response.put("cities", citiesDTO);
        response.put("status", HttpStatus.OK.value());
        return new ResponseEntity<LinkedHashMap<String, Object>>(response, HttpStatus.OK);
    }
}
