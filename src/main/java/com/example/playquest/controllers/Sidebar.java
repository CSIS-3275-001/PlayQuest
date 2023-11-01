package com.example.playquest.controllers;

import com.example.playquest.entities.Ads;
import com.example.playquest.entities.Notification;
import com.example.playquest.entities.User;
import com.example.playquest.repositories.AdsRepository;
import com.example.playquest.repositories.NotificationRepository;
import com.example.playquest.repositories.UsersRepository;
import com.example.playquest.services.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class Sidebar {

    private final SessionManager sessionManager;
    private final UsersRepository usersRepository;
    private final AdsRepository adsRepository;
    private final NotificationRepository notificationRepository;

    @GetMapping(path = "/notifications")
    public String Notifications(Model model, HttpServletRequest request) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        // Get the last three created URLs
        List<Ads> lastThreeAds = adsRepository.findTop3ByOrderByCreatedOnDesc();
        List<String> lastThreeAdsUrl = lastThreeAds.stream().map(Ads::getUrl).toList();

        model.addAttribute("lastThreeAdsUrl", lastThreeAdsUrl);

        Long userId = sessionManager.getUserId(request);
        User user = usersRepository.findById(userId).orElse(null);

        // Fetch notifications for the current user
        List<Notification> notifications = notificationRepository.findByReceiver(user);

        model.addAttribute("notifications", notifications);
        model.addAttribute("user", user);

        return "notifications";
    }

    @DeleteMapping(path = "/notification/{id}")
    public String deleteNotification(@PathVariable Long id) {
        notificationRepository.deleteById(id);

        // Redirect to the notifications page after deletion
        return "redirect:/notifications";
    }

    @GetMapping(path = "/settings")
    public String Settings(Model model, HttpServletRequest request) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        Long userId = sessionManager.getUserId(request);
        User user = usersRepository.findById(userId).orElse(null);

        // Get the last three created URLs
        List<Ads> lastThreeAds = adsRepository.findTop3ByOrderByCreatedOnDesc();
        List<String> lastThreeAdsUrl = lastThreeAds.stream().map(Ads::getUrl).toList();

        model.addAttribute("lastThreeAdsUrl", lastThreeAdsUrl);
        model.addAttribute("user", user);

        return "settings";
    }

}
