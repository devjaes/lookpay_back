package dev.jeep.Lookpay.services;

import java.util.LinkedHashMap;

import org.springframework.http.ResponseEntity;

import dev.jeep.Lookpay.dtos.UserRegisterDTO;
import dev.jeep.Lookpay.dtos.UserModifyDTO;

public abstract class CrudHandler {
    public ResponseEntity<LinkedHashMap<String, Object>> create(UserRegisterDTO user, String gender) {
        return null;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> get(Long id) {
        return null;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> update(Long id, UserModifyDTO user, String rol) {
        return null;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> delete(Long id, String rol) {
        return null;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> delete(Long id) {
        return null;
    }
}
