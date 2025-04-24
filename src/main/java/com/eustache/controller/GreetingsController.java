package com.eustache.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class GreetingsController {
    
    @GetMapping("/hello")
    public String getMethodName() {
        return "Hello from spring security";
    }
    
}
