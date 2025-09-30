package org.aps.delta.smallservice.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmallRestController {

    @GetMapping("small/sayHello")
    public String sayHello() {
        return "Привет из smallservice!";
    }
}
