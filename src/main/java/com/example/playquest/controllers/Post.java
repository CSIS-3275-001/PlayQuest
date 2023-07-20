package com.example.playquest.controllers;

import com.example.playquest.services.SessionManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class Post {

    private SessionManager sessionManager;

    @GetMapping(path = "/create")
    public String Create(HttpServletRequest request) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

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
            @RequestParam("toggleStatus") Boolean toggleStatus,
            @RequestParam("spinnerSelection") String spinnerSelection
    ) {
        String uploadDir = "src/main/resources/static/images/posts/";

        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

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

        // Now you have access to all the parameters provided in the POST request.
        // You can use them in your business logic as required.

        // Example usage:
        System.out.println("Title: " + title);
        System.out.println("Images: " + fileNames);
        System.out.println("Description: " + description);

        // If the user is logged in, proceed with the home page logic
        model.addAttribute("message", "success");
        return "/create"; // Replace this with the appropriate return value for your case
    }
}
