package com.example.playquest.controllers;

import com.example.playquest.entities.PostContent;
import com.example.playquest.entities.User;
import com.example.playquest.repositories.PostContentRepository;
import com.example.playquest.repositories.UsersRepository;
import com.example.playquest.services.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class Home {

    private final SessionManager sessionManager;
    private final UsersRepository usersRepository;

    private final PostContentRepository postContentRepository;

    @GetMapping("/")
    public String Index(Model model, HttpServletRequest request) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        Long userId = sessionManager.getUserId(request); // Assuming you have a method to retrieve the userId from the session
        User user = usersRepository.findById(userId).orElse(null); // Assuming you have a UserRepository for querying user details


        List<PostContent> postContents = postContentRepository.findAll();
        model.addAttribute("postContents", postContents);
        model.addAttribute("user", user);


        // If the user is logged in, proceed with the home page logic
        return "index";
    }



}
