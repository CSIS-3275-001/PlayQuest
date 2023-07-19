package com.example.playquest.controllers;

import com.example.playquest.services.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    private final SessionManager sessionManager;

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


}
