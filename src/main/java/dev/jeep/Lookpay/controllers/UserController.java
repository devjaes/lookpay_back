package dev.jeep.Lookpay.controllers;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/user") // http://localhost:8080/user
public class UserController {

    @GetMapping("/greetings1")
    public ResponseEntity<HashMap<String, String>> hello() {
        return new ResponseEntity<HashMap<String, String>>(new HashMap<String, String>() {
            {
                put("message", "Hello World");
            }
        }, HttpStatus.OK);
    }
}
