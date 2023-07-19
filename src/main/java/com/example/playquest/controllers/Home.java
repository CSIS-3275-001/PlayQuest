package com.example.playquest.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class Home {
    @Autowired
    @GetMapping(path = "/")
    public String Index() {
        return "index";
    }

    @Autowired
    @GetMapping(path="/admin")
    public String Admin(){
        return "admin";
    }

}
