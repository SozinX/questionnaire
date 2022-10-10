package com.sozinx.questionnaire.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WelcomeController {
    @RequestMapping("/questionnaire")
    public String questionnaire() {
        return "questionnaire";
    }

}
