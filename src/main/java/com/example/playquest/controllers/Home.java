package com.example.playquest.controllers;

import com.example.playquest.entities.Ads;
import com.example.playquest.entities.PostContent;
import com.example.playquest.entities.User;
import com.example.playquest.repositories.AdsRepository;
import com.example.playquest.entities.Notification;
import com.example.playquest.entities.PostContent;
import com.example.playquest.entities.User;
import com.example.playquest.repositories.NotificationRepository;
import com.example.playquest.repositories.PostContentRepository;
import com.example.playquest.repositories.UsersRepository;
import com.example.playquest.services.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class Home {

    private final SessionManager sessionManager;
    private final UsersRepository usersRepository;

    private final PostContentRepository postContentRepository;
    private final AdsRepository adsRepository;

    @GetMapping("/")
    public String Index(Model model, HttpServletRequest request) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        Long userId = sessionManager.getUserId(request); // Assuming you have a method to retrieve the userId from the session
        User user = usersRepository.findById(userId).orElse(null); // Assuming you have a UserRepository for querying user details

        List<User>  allUsers = usersRepository.findAll();
        List<PostContent> postContents = postContentRepository.findAll();

        // Get the last three created URLs
        List<Ads> lastThreeAds = adsRepository.findTop3ByOrderByCreatedByDesc();
        List<String> lastThreeAdsUrl = lastThreeAds.stream().map(Ads::getUrl).toList();


        model.addAttribute("postContents", postContents);
        model.addAttribute("user", user);
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("lastThreeAdsUrl", lastThreeAdsUrl);

        // If the user is logged in, proceed with the home page logic
        return "index";
    }




}
