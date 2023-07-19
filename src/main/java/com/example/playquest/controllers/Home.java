package com.example.playquest.controllers;

import com.example.playquest.entities.UserSession;
import com.example.playquest.services.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class Home {

    private final SessionManager sessionManager;

    @GetMapping("/")
    public String Index(HttpServletRequest request) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        // If the user is logged in, proceed with the home page logic
        return "index";
    }

    @Autowired
    @GetMapping(path="/admin")
    public String Admin(){
        return "admin";
    }

}
