package com.example.playquest;

import com.example.playquest.controllers.Home;
import com.example.playquest.entities.Ads;
import com.example.playquest.entities.PostContent;
import com.example.playquest.entities.User;
import com.example.playquest.repositories.AdsRepository;
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

public class HomeTest {
    private SessionManager sessionManager;
    private UsersRepository usersRepository;
    private PostContentRepository postContentRepository;
    private AdsRepository adsRepository;
    private HttpServletRequest request;
    private Model model;
    private Home home;

    @BeforeEach
    void setUp() {
        sessionManager = mock(SessionManager.class);
        usersRepository = mock(UsersRepository.class);
        postContentRepository = mock(PostContentRepository.class);
        adsRepository = mock(AdsRepository.class);
        request = mock(HttpServletRequest.class);
        model = mock(Model.class);

        home = new Home(sessionManager, usersRepository, postContentRepository, adsRepository);
    }

    @Test
    void indexWhenUserNotLoggedIn() {
        when(sessionManager.isUserLoggedIn(request)).thenReturn(false);
        String result = home.Index(model, request);
        assertEquals("redirect:/login", result);
    }

    @Test
    void indexWhenUserLoggedIn() {
        when(sessionManager.isUserLoggedIn(request)).thenReturn(true);
        when(sessionManager.getUserId(request)).thenReturn(1L);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(new User()));

        List<User> users = List.of(new User(), new User());
        when(usersRepository.findAll()).thenReturn(users);

        List<PostContent> postContents = List.of(new PostContent(), new PostContent());
        when(postContentRepository.findAll()).thenReturn(postContents);

        List<Ads> ads = List.of(new Ads(), new Ads(), new Ads());
        when(adsRepository.findTop3ByOrderByCreatedOnDesc()).thenReturn(ads);

        String result = home.Index(model, request);

        assertEquals("index", result);
        verify(model, times(1)).addAttribute("user", users.get(0));
        verify(model, times(1)).addAttribute("allUsers", users);
        verify(model, times(1)).addAttribute("postContents", postContents);
        verify(model, times(1)).addAttribute(eq("lastThreeAdsUrl"), anyList());
    }
}
