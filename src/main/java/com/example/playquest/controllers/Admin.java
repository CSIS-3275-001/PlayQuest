package com.example.playquest.controllers;

import com.example.playquest.entities.Game;
import com.example.playquest.entities.User;
import com.example.playquest.repositories.GameRepository;
import com.example.playquest.repositories.UsersRepository;
import com.example.playquest.services.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class Admin {

    private final UsersRepository usersRepository;
    private final SessionManager sessionManager;
    private final GameRepository gameRepository;

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

    @GetMapping("/admin/users")
    public String Users(HttpServletRequest request, Model model) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        List<User> users = usersRepository.findAll();
        model.addAttribute("users", users);

        // If the user is logged in, proceed with the home page logic
        return "admin/users";
    }

    @GetMapping("/admin/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/admin/users/create")
    public String createUser(@ModelAttribute User user) {
        usersRepository.save(user);
        return "admin/users";
    }

    @GetMapping("/admin/users/update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "redirect:/admin/users/update";
    }

    @PostMapping("/admin/users/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute User user, Model model) {
        // Save user after setting id to ensure it updates, not creates new
        user.setId(id);
        usersRepository.save(user);
        return "admin/users";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        usersRepository.delete(user);
        return "redirect:/admin/users";
    }


    @GetMapping("/admin/games")
    public String Games(Model model, HttpServletRequest request) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        List<Game> games = gameRepository.findAll();
        model.addAttribute("games", games);

        // If the user is logged in, proceed with the home page logic
        return "admin/games";
    }

    @PostMapping("/admin/games/save")
    public String saveGame(@ModelAttribute Game game) {
        gameRepository.save(game);
        return "redirect:/admin/games";
    }

    @PostMapping("/admin/ads")
    public String InserAds(Model model, HttpServletRequest request, @RequestParam("photos") MultipartFile[] photos) {
        String uploadDir = "src/main/resources/static/images/ads";

        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        // Process the uploaded files
        // Process the uploaded files
        List<String> fileNames = new ArrayList<>();
        int count = 1; // Counter for file names
        for (MultipartFile photo : photos) {
            // Perform necessary operations with the uploaded file
            String fileExtension = StringUtils.getFilenameExtension(photo.getOriginalFilename());
            String fileName = count + "." + fileExtension;
            try {
                // Save the file to the static directory

                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                try (InputStream inputStream = photo.getInputStream()) {
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    fileNames.add(fileName);
                }

                count++;
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception as per your application's requirements
            }
        }

        System.out.println(fileNames);

        // If the user is logged in, proceed with the home page logic
        model.addAttribute("message", "success");
        return "admin/ads";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {

        public ResourceNotFoundException(String message) {
            super(message);
        }

        public ResourceNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
