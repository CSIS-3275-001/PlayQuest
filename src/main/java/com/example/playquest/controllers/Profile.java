package com.example.playquest.controllers;

import com.example.playquest.entities.Game;
import com.example.playquest.entities.GameProfile;
import com.example.playquest.repositories.GameProfileRepository;
import com.example.playquest.repositories.GameRepository;
import com.example.playquest.services.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class Profile {

    private final GameProfileRepository gameProfileRepository;

    private final SessionManager sessionManager;
    @GetMapping(path = "/profile")
    public String Profile(Model model, HttpServletRequest request) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        List<GameProfile> gamesProfiles = gameProfileRepository.findAll();
        model.addAttribute("gamesProfiles", gamesProfiles);

        System.out.println("games = " + gamesProfiles);

        return "profile";
    }
}
