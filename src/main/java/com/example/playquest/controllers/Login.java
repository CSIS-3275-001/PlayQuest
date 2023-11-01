package com.example.playquest.controllers;

import com.example.playquest.entities.User;
import com.example.playquest.repositories.UsersRepository;
import com.example.playquest.services.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
public class Login {

    private UsersRepository usersRepository;
    private final SessionManager sessionManager;

    public Login(SessionManager sessionManager, UsersRepository usersRepository) {
        this.sessionManager = sessionManager;
        this.usersRepository = usersRepository;
    }

    @GetMapping("/login")
    public String showLoginPage(HttpServletRequest request) {

        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(@RequestParam("email") String email, @RequestParam("password") String password,
            HttpServletResponse response) {
        // Authenticate the user based on email and password
        User user = authenticateUserByEmailAndPassword(email, password);

        if (user != null) {
            // If the user is authenticated, create a session
            String sessionId = generateSessionId();
            Long userId = user.getId();
            LocalDateTime expirationTime = calculateSessionExpirationTime();

            System.out.println("Session ID here: " + sessionId);
            sessionManager.createSession(sessionId, userId, expirationTime, response);

            // Return the session ID to the client (e.g., as a cookie or response body)
            return new RedirectView("/");
        } else {
            // Authentication failed, return an error response
            return new RedirectView("/login?error");
        }
    }

    private User authenticateUserByEmailAndPassword(String email, String password) {
        Optional<User> userOptional = usersRepository.findByEmail(email);
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            return userOptional.get();
        } else {
            return null;
        }
    }

    // @PostMapping("/logout")
    // public ResponseEntity<String> logout(@RequestParam("sessionId") String
    // sessionId) {
    // sessionManager.deleteSession(sessionId);
    //
    // return ResponseEntity.ok("Logged out successfully");
    // }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = sessionManager.getSessionIdFromCookie(request);

        System.out.println("Session ID logout: " + sessionId);
        sessionManager.deleteSession(sessionId);
        sessionManager.deleteSessionIdCookie(response);
        // Redirect to the login page or any other desired destination
        return "redirect:/login";
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    private LocalDateTime calculateSessionExpirationTime() {
        return LocalDateTime.now().plusHours(1); // Expiration time is set to 1 hour from the current time
    }

}
