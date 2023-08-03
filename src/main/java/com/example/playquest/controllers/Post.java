package com.example.playquest.controllers;

import com.example.playquest.AwsConfig;
import com.example.playquest.entities.Ads;
import com.example.playquest.entities.GameProfile;
import com.example.playquest.entities.Notification;
import com.example.playquest.entities.PostContent;
import com.example.playquest.entities.User;
import com.example.playquest.repositories.*;
import com.example.playquest.services.SessionManager;
import lombok.AllArgsConstructor;
import netscape.javascript.JSObject;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class Post {

    private SessionManager sessionManager;
    private final GameProfileRepository gameProfileRepository;
    private final PostContentRepository postContentRepository;
    private final NotificationRepository notificationRepository;
    private final UsersRepository usersRepository;
    private final AdsRepository adsRepository;

    @Autowired
    private AwsConfig awsConfig;

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

    @GetMapping(path = "/post/{id}")
    public String getPost(@PathVariable Long id, Model model, HttpServletRequest request) {
        Long userId = sessionManager.getUserId(request); // Assuming you have a method to retrieve the userId from the session
        User user = usersRepository.findById(userId).orElse(null); // Assuming you have a UserRepository for querying user details

        // Fetch the post using the id
        PostContent postContent = postContentRepository.findById(id).orElse(null);

        // Get the last three created URLs
        List<Ads> lastThreeAds = adsRepository.findTop3ByOrderByCreatedOnDesc();
        List<String> lastThreeAdsUrl = lastThreeAds.stream().map(Ads::getUrl).toList();


        // Add the post content to the model
        model.addAttribute("user", user);
        model.addAttribute("post", postContent);
        model.addAttribute("lastThreeAdsUrl", lastThreeAdsUrl);

        // Return the view that will display the post content
        return "post"; // Update this with the name of your view that shows the post details
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
        // Name of your S3 bucket
        String bucketName = "playquest-proj";

        // Base URL for files in your S3 bucket
        String baseURL = "https://playquest-proj.s3.ap-southeast-1.amazonaws.com/";

        if(toggleStatus == null){
            toggleStatus = false;
        }
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }


        System.out.println("Inside POST create");

        // Process the uploaded files
        List<String> fileNames = new ArrayList<>();

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
                    String imageUrl = baseURL + fileName;
                    fileNames.add(imageUrl);
                }

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
        postContent.setUserId(userId);

        // Save the PostContent object to the repository
        postContentRepository.save(postContent);

        // Example usage:
        System.out.println("Title: " + title);
        System.out.println("Ads: " + fileNames);
        System.out.println("Description: " + description);

        // If the user is logged in, proceed with the home page logic
        model.addAttribute("message", "success");
        model.addAttribute("gamesProfiles", gamesProfiles);
        model.addAttribute("user", user);
        return "redirect:/"; // Replace this with the appropriate return value for your case
    }

    @PostMapping(path = "/updateLikes")
    public String updateLikes(Model model, HttpServletRequest request, @org.springframework.web.bind.annotation.RequestBody Map<String, Long> data) {
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

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postContentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping(path = "/sendnotify")
    public String Notify(Model model, HttpServletRequest request, @RequestParam("id_post") long postId, @RequestParam("message") String message, @RequestParam("user_id") long receiverId) {
        // Check if the user is logged in or has an active session
        if (!sessionManager.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        System.out.println(receiverId + "<<");

        Long senderId = sessionManager.getUserId(request); // Assuming you have a method to retrieve the userId from the session
        Notification notification = new Notification();
        notification.setNotification_message(message);
        notification.setSender(usersRepository.findById(senderId).orElse(null));
        notification.setReceiver(usersRepository.findById(receiverId).orElse(null));
        Optional<PostContent> optionalPostContent = postContentRepository.findById(postId);
        optionalPostContent.ifPresent(notification::setPost);
        System.out.println("notification receiver= " + notification.getReceiver().getId());
        notificationRepository.save(notification);

        return "redirect:/";
    }
}
