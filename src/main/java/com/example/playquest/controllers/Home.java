package com.example.playquest.controllers;

import com.example.playquest.entities.PostContent;
import com.example.playquest.entities.UserSession;
import com.example.playquest.repositories.PostContentRepository;
import com.example.playquest.services.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class Home {

    private final SessionManager sessionManager;

    private final PostContentRepository postContentRepository;

    @GetMapping("/")
    public String Index(Model model, HttpServletRequest request) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        List<PostContent> postContents = postContentRepository.findAll();
        model.addAttribute("postContents", postContents);

        // If the user is logged in, proceed with the home page logic
        return "index";
    }



}
