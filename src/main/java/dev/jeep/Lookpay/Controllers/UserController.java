package dev.jeep.Lookpay.Controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/user") // http://localhost:8080/user
public class UserController {

    @GetMapping("/")
    public String hello() {
        return "Hello World!";
    }
}
