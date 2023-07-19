package com.example.playquest.controllers;

import com.example.playquest.entities.User;
import com.example.playquest.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class Login {

    @Autowired
    private UsersRepository usersRepository;


    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(Model model, @RequestParam("email") String email, @RequestParam("password") String password) {
        Optional<User> user = usersRepository.findByEmail(email);
        System.out.println("M here");
        System.out.println(user);
        if (user.isPresent()) {
            User u = user.get();
            if (u.getPassword().equals(password)) {
                System.out.println("M here yes");
                return "redirect:/";
            }
        }

        model.addAttribute("error", "Invalid email or password."); // Add the error message to the model
        return "login"; // Return to the login page
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        return "/login"; // Return to the login page
    }

}
