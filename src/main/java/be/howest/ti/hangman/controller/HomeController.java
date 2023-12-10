package be.howest.ti.hangman.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(value = "*", allowedHeaders = "*")
public class HomeController {

    @GetMapping("/status")
    public String home() {
        return "Up and running";
    }

}
