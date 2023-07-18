package com.example.playquest.controllers;

import com.example.playquest.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class Sidebar {

    @Autowired
    @GetMapping(path = "/post/create")
    public String uploadPost(){
        return "uploadContent";
    }

    @PostMapping(path = "/post/create")
    public String upload() {
        return "redirect:/"; // Redirect to the login page after successful signup
    }
}
