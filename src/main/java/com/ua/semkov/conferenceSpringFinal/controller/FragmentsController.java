package com.ua.semkov.conferenceSpringFinal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FragmentsController {



    @GetMapping("/fragments")
    public String getHome() {
        return "/fragments/fragments";
    }

    @GetMapping("/header")
    public String headerPage() {
        return "/fragments/header";
    }

    @GetMapping("/markup")
    public String markupPage() {
        return "/fragments/markup";
    }

    @GetMapping("/params")
    public String paramsPage() {
        return "/fragments/params";
    }

}
