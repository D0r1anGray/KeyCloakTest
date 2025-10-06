package org.aps.delta.smallservice.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/small")
public class SmallRestController {

    @GetMapping("/sayHelloAdmin")
    public String sayHelloAdmin() {
        return "Привет Админу из smallservice!";
    }

    @GetMapping("/sayHelloUser")
    public String sayHelloUser() {
        return "Привет Юзеру из smallservice!";
    }
}
