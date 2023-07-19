package com.example.playquest.controllers;

import com.example.playquest.services.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class Admin {

    private final SessionManager sessionManager;

    @GetMapping("/admin")
    public String Administrator(HttpServletRequest request) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        // If the user is logged in, proceed with the home page logic
        return "admin/index";
    }

    @GetMapping("/admin/ads")
    public String Ads(HttpServletRequest request) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        // If the user is logged in, proceed with the home page logic
        return "admin/ads";
    }


}
