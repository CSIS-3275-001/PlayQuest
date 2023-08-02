package com.example.playquest;

import com.example.playquest.controllers.Admin;
import com.example.playquest.entities.Ads;
import com.example.playquest.entities.Game;
import com.example.playquest.entities.User;
import com.example.playquest.repositories.AdsRepository;
import com.example.playquest.repositories.GameRepository;
import com.example.playquest.repositories.PostContentRepository;
import com.example.playquest.repositories.UsersRepository;
import com.example.playquest.services.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AdminTest {
    private UsersRepository usersRepository;
    private SessionManager sessionManager;
    private GameRepository gameRepository;
    private PostContentRepository postContentRepository;
    private AdsRepository adsRepository;
    private AwsConfig awsConfig;
    private HttpServletRequest request;
    private Model model;
    private Admin admin;

    @BeforeEach
    void setUp() {
        usersRepository = mock(UsersRepository.class);
        sessionManager = mock(SessionManager.class);
        gameRepository = mock(GameRepository.class);
        postContentRepository = mock(PostContentRepository.class);
        adsRepository = mock(AdsRepository.class);
        awsConfig = mock(AwsConfig.class);
        request = mock(HttpServletRequest.class);
        model = mock(Model.class);

        admin = new Admin(usersRepository, sessionManager, gameRepository, postContentRepository, adsRepository, awsConfig);
    }

    @Test
    void AdministratorWhenUserNotLoggedIn() {
        when(sessionManager.isUserLoggedIn(request)).thenReturn(false);
        String result = admin.Administrator(request, model);
        assertEquals("redirect:/login", result);
    }

    @Test
    void AdministratorWhenUserLoggedIn() {
        when(sessionManager.isUserLoggedIn(request)).thenReturn(true);
        when(usersRepository.count()).thenReturn(10L);
        when(gameRepository.count()).thenReturn(20L);
        when(postContentRepository.count()).thenReturn(30L);
        when(adsRepository.count()).thenReturn(5L);

        admin.Administrator(request, model);

        verify(sessionManager, times(1)).isUserLoggedIn(request);
        verify(usersRepository, times(1)).count();
        verify(gameRepository, times(1)).count();
        verify(postContentRepository, times(1)).count();
        verify(adsRepository, times(1)).count();

        verify(model, times(1)).addAttribute("totalUsers", 10L);
        verify(model, times(1)).addAttribute("totalGames", 20L);
        verify(model, times(1)).addAttribute("totalPosts", 30L);
        verify(model, times(1)).addAttribute("totalImages", 35L);
    }
}
