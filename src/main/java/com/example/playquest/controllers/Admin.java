package com.example.playquest.controllers;

import com.example.playquest.AwsConfig;
import com.example.playquest.entities.Ads;
import com.example.playquest.entities.Game;
import com.example.playquest.entities.User;
import com.example.playquest.repositories.AdsRepository;
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

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class Admin {

    private final UsersRepository usersRepository;
    private final SessionManager sessionManager;
    private final GameRepository gameRepository;

    @Autowired
    private AdsRepository adsRepository;

    @Autowired
    private AwsConfig awsConfig;


    @GetMapping("/admin")
    public String Administrator(HttpServletRequest request, Model model) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        long totalUsers = usersRepository.count();
        long totalGames = gameRepository.count();
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalGames", totalGames);

        // If the user is logged in, proceed with the home page logic
        return "admin/index";
    }

    @GetMapping("/admin/ads")
    public String Ads(HttpServletRequest request, Model model) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        // Get the last three created URLs
        List<Ads> lastThreeAds = adsRepository.findTop3ByOrderByCreatedByDesc();
        List<String> lastThreeAdsUrl = lastThreeAds.stream().map(Ads::getUrl).toList();

        model.addAttribute("lastThreeAdsUrl", lastThreeAdsUrl);

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
    public String InsertAds(Model model, HttpServletRequest request, @RequestParam("photos") MultipartFile[] photos) {

        // Name of your S3 bucket
        String bucketName = "playquest-proj";

        // Base URL for files in your S3 bucket
        String baseURL = "https://playquest-proj.s3.ap-southeast-1.amazonaws.com/";

        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        // Process the uploaded files
        List<String> fileNames = new ArrayList<>();
        List<String> fileLinks = new ArrayList<>(); // To store the file links

        // Create an S3 client
        S3Client s3Client = awsConfig.s3Client();

        for (MultipartFile photo : photos) {
            // Generate a unique file name
            String fileExtension = StringUtils.getFilenameExtension(photo.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + "." + fileExtension;

            System.out.println("Uploading " + fileName);

            try {
                // Upload the file to S3
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .acl(ObjectCannedACL.PUBLIC_READ)
                        .build();

                PutObjectResponse response = s3Client.putObject(putObjectRequest,
                        RequestBody.fromInputStream(photo.getInputStream(), photo.getSize()));

                if(response != null) {
                    fileNames.add(fileName);
                    String imageUrl = baseURL + fileName;
                    fileLinks.add(imageUrl); // Add the link to the list

                    // Save the URL in the database
                    Ads newAd = new Ads();
                    newAd.setUrl(imageUrl);
                    newAd.setCreatedBy(sessionManager.getUserId(request));
                    adsRepository.save(newAd);
                }

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
