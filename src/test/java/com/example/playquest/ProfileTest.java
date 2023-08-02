package com.example.playquest;

import com.example.playquest.controllers.Profile;
import com.example.playquest.entities.*;
import com.example.playquest.repositories.*;
import com.example.playquest.services.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProfileTest {
    @InjectMocks
    Profile profileController;

    @Mock
    GameProfileRepository gameProfileRepository;

    @Mock
    GameRepository gameRepository;

    @Mock
    UsersRepository usersRepository;

    @Mock
    PostContentRepository postContentRepository;

    @Mock
    SessionManager sessionManager;

    @Mock
    HttpServletRequest request;

    @Mock
    Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProfile() {
        when(sessionManager.isUserLoggedIn(request)).thenReturn(true);
        when(sessionManager.getUserId(request)).thenReturn(1L);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(gameProfileRepository.findAll()).thenReturn(Collections.emptyList());
        when(gameRepository.findAll()).thenReturn(Collections.emptyList());
        when(postContentRepository.findAll()).thenReturn(Collections.emptyList());

        String result = profileController.Profile(model, request);
        assertEquals("profile", result);
    }

    @Test
    public void testSaveGameProfile() {
        GameProfile gameProfile = new GameProfile();
        when(sessionManager.getUserId(request)).thenReturn(1L);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(gameProfileRepository.save(any(GameProfile.class))).thenReturn(gameProfile);

        String result = profileController.saveGameProfile(gameProfile, request);
        assertEquals("redirect:/profile", result);
    }
}
