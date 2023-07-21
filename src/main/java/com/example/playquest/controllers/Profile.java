package com.example.playquest.controllers;

import com.example.playquest.entities.Game;
import com.example.playquest.entities.GameProfile;
import com.example.playquest.entities.User;
import com.example.playquest.repositories.GameProfileRepository;
import com.example.playquest.repositories.GameRepository;
import com.example.playquest.repositories.UsersRepository;
import com.example.playquest.services.SessionManager;
import com.example.playquest.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class Profile {

    private final GameProfileRepository gameProfileRepository;
    private final UsersRepository usersRepository;


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

    @GetMapping(path = "/profile/edit")
    public String ProfileEdit(Model model, HttpServletRequest request) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        // Retrieve the userId from the session manager
        Long userId = sessionManager.getUserId(request); // Assuming you have a method to retrieve the userId from the session

        // Retrieve the user details from the repository
        User user = usersRepository.findById(userId).orElse(null); // Assuming you have a UserRepository for querying user details

        // Add user details to the model
        model.addAttribute("user", user);

        return "edit_profile";
    }

    @PostMapping("/updateProfile/{userId}")
    public String updateProfile(@PathVariable("userId") Long userId, @ModelAttribute("user") User updatedUser) {
        // Retrieve the existing user from the repository
        User existingUser = usersRepository.findById(userId).orElse(null);

        // Check if the user exists
        if (existingUser != null) {
            // Update the user's attributes
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setName(updatedUser.getName());

            // Save the updated user in the repository
            usersRepository.save(existingUser);
        }

        return "redirect:/profile"; // Redirect to the profile page after updating
    }

}
