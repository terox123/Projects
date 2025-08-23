package com.UserPassportBoot.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstPageController {

    @GetMapping("/")
    public String first(){
        return "first/HelloPage";
    }


}
