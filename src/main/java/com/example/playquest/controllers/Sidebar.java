package com.example.playquest.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class Sidebar {

        @Autowired
        @GetMapping(path = "/explore")
        public String Explore() {
            return "explore";
        }

        @Autowired
        @GetMapping(path = "/create")
        public String Create() {
            return "create";
        }

        @Autowired
        @GetMapping(path = "/settings")
        public String Settings() {
            return "settings";
        }

}
