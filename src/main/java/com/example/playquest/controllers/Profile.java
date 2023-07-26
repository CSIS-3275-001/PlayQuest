package com.example.playquest.controllers;

import com.example.playquest.entities.Game;
import com.example.playquest.entities.GameProfile;
import com.example.playquest.entities.PostContent;
import com.example.playquest.entities.User;
import com.example.playquest.repositories.GameProfileRepository;
import com.example.playquest.repositories.GameRepository;
import com.example.playquest.repositories.PostContentRepository;
import com.example.playquest.repositories.UsersRepository;
import com.example.playquest.services.SessionManager;
import com.example.playquest.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class Profile {

    private final GameProfileRepository gameProfileRepository;
    private final GameRepository gameRepository;
    private final UsersRepository usersRepository;
    private final PostContentRepository postContentRepository;

    private final SessionManager sessionManager;

    @Autowired
    private HttpSession httpSession; // Autowire HttpSession to access the session


    @GetMapping(path = "/profile")
    public String Profile(Model model, HttpServletRequest request) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        Long userId = sessionManager.getUserId(request); // Assuming you have a method to retrieve the userId from the session
        User user = usersRepository.findById(userId).orElse(null); // Assuming you have a UserRepository for querying user details
        List<GameProfile> gamesProfiles = gameProfileRepository.findAll();
        List<Game> games = gameRepository.findAll();
        List<PostContent> postContents = postContentRepository.findAll();
        System.out.println("postContents = " + postContents);

        model.addAttribute("postContents", postContents);

        model.addAttribute("gamesProfiles", gamesProfiles);
        model.addAttribute("games", games);
        model.addAttribute("user", user);
        model.addAttribute("gameProfile", new GameProfile());

        System.out.println("games = " + gamesProfiles);

        return "profile";
    }

    @PostMapping("/save-game-profile")
    public String saveGameProfile(@ModelAttribute("gameProfile") GameProfile gameProfile, HttpServletRequest request) {
        // Set the createdOn property to the current date and time
        gameProfile.setCreatedOn(LocalDateTime.now());

        // Get the userId from the session using sessionManager
        Long userId = sessionManager.getUserId(request);
        User user = usersRepository.findById(userId).orElse(null);

        // Set the user as the userId in the gameProfile
        gameProfile.setUserId(user);

        // Save the game profile in the database using gameProfileRepository
        gameProfileRepository.save(gameProfile);
        return "redirect:/profile"; // Redirect to the home page after saving
    }

    @PostMapping("/delete-game-profile")
    public String deleteGameProfile(@RequestParam("profileId") Long profileId) {
        // Retrieve the GameProfile to be deleted from the database
        gameProfileRepository.findById(profileId).ifPresent(gameProfileRepository::delete);

        return "redirect:/profile"; // Redirect to the home page after deletion
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
    public String updateProfile(Model model,@PathVariable("userId") Long userId, @ModelAttribute("user") User updatedUser) {
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

        model.addAttribute("user", existingUser);

        return "redirect:/profile"; // Redirect to the profile page after updating
    }

}
