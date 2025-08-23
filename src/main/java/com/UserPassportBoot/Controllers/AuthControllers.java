package com.UserPassportBoot.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthControllers {

@GetMapping("/login")
public String login(){
    return "login";
}
@GetMapping("/home")
public String home(){
    return "first/HelloPage";
}
}
