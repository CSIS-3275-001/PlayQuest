package com.example.playquest.controllers;

import com.example.playquest.entities.GameProfile;
import com.example.playquest.entities.PostContent;
import com.example.playquest.entities.User;
import com.example.playquest.repositories.GameProfileRepository;
import com.example.playquest.repositories.PostContentRepository;
import com.example.playquest.repositories.UsersRepository;
import com.example.playquest.services.SessionManager;
import lombok.AllArgsConstructor;
import netscape.javascript.JSObject;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Controller
@AllArgsConstructor
public class Post {

    private SessionManager sessionManager;
    private final GameProfileRepository gameProfileRepository;
    private final PostContentRepository postContentRepository;
    private final UsersRepository usersRepository;

    @GetMapping(path = "/create")
    public String Create(Model model,HttpServletRequest request) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        Long userId = sessionManager.getUserId(request); // Assuming you have a method to retrieve the userId from the session
        User user = usersRepository.findById(userId).orElse(null); // Assuming you have a UserRepository for querying user details


        List<GameProfile> gamesProfiles = gameProfileRepository.findAll();
        model.addAttribute("gamesProfiles", gamesProfiles);
        model.addAttribute("user", user);

        System.out.println("games = " + gamesProfiles);

        return "create";
    }

    // This function handles the POST request for your postContent page
    @PostMapping(path = "/create")
    public String postContent(
            Model model,
            HttpServletRequest request,
            @RequestParam("title") String title,
            @RequestParam("photos") MultipartFile[] photos,
            @RequestParam("description") String description,
            @RequestParam(value = "toggleStatus", required = false) Boolean toggleStatus,
            @RequestParam("spinnerSelection") String spinnerSelection
    ) {
        String uploadDir = "src/main/resources/static/images/posts/";

        if(toggleStatus== null){
            toggleStatus = false;
        }
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }
        System.out.println("Inside POST create");
        // Process the uploaded files
        List<String> fileNames = new ArrayList<>();
        int count = 1; // Counter for file names
        for (MultipartFile photo : photos) {
            // Perform necessary operations with the uploaded file
            String fileExtension = StringUtils.getFilenameExtension(photo.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + count + "." + fileExtension;
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

        Long userId = sessionManager.getUserId(request); // Assuming you have a method to retrieve the userId from the session
        User user = usersRepository.findById(userId).orElse(null); // Assuming you have a UserRepository for querying user details


        List<GameProfile> gamesProfiles = gameProfileRepository.findAll();



        // Now you have access to all the parameters provided in the POST request.
        // You can use them in your business logic as required.
        PostContent postContent = new PostContent();
        postContent.setTitle(title);
        postContent.setImages(fileNames); // Assuming you only save one photo per post
        postContent.setDescription(description);
        postContent.setToggleStatus(toggleStatus);
        postContent.setSpinnerSelection(spinnerSelection);// Replace "John Doe" with the actual profile name
        postContent.setLikes(0);
        // Save the PostContent object to the repository
        postContentRepository.save(postContent);
        // Example usage:
        System.out.println("Title: " + title);
        System.out.println("Images: " + fileNames);
        System.out.println("Description: " + description);

        // If the user is logged in, proceed with the home page logic
        model.addAttribute("message", "success");
        model.addAttribute("gamesProfiles", gamesProfiles);
        model.addAttribute("user", user);
        return "/create"; // Replace this with the appropriate return value for your case
    }

    @PostMapping(path = "/updateLikes")
    public String updateLikes(Model model, HttpServletRequest request, @RequestBody Map<String, Long> data) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        System.out.println("data = " + data);
        Long postID = data.get("id");
        int updatedLikes = data.get("likes").intValue();

        // Assuming you have a PostContentRepository instance defined somewhere.
        // Make sure to handle any potential errors or exceptions related to database operations.
        Optional<PostContent> postOptional = postContentRepository.findById(postID);
        if (postOptional.isPresent()) {
            PostContent postContent = postOptional.get();
            postContent.setLikes(updatedLikes);
            postContentRepository.save(postContent);
        } else {
            // Handle the error as per your application's requirements
        }

        System.out.println("data = " + postID + " " + updatedLikes);

        return "redirect:/";
    }


}
