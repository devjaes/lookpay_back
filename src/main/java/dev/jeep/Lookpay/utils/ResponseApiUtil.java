package dev.jeep.Lookpay.utils;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseApiUtil {
    public ResponseEntity<HashMap<String, Object>> handleResponse(String message, HttpStatus status) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", status.value());

        return new ResponseEntity<HashMap<String, Object>>(response, status);
    }
}