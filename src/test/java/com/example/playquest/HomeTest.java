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
import org.mockito.*;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class HomeTest {

    @InjectMocks
    private Home homeController;

    @Mock
    private SessionManager sessionManager;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PostContentRepository postContentRepository;

    @Mock
    private AdsRepository adsRepository;

    @Mock
    private Model model;

    @Mock
    private HttpServletRequest request;

    private static final Long USER_ID = 1L;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void indexWhenUserNotLoggedIn() {
        when(sessionManager.isUserLoggedIn(request)).thenReturn(false);
        String result = homeController.Index(model, request);
        assertEquals("redirect:/login", result);
    }

    @Test
    public void indexWhenUserLoggedIn() {
        // setup
        when(sessionManager.isUserLoggedIn(request)).thenReturn(true);
        when(sessionManager.getUserId(request)).thenReturn(USER_ID);
        User user = new User();
        when(usersRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        List<User> users = List.of(new User(), new User());
        when(usersRepository.findAll()).thenReturn(users);

        List<PostContent> postContents = List.of(new PostContent(), new PostContent());
        when(postContentRepository.findAllByOrderByCreatedOnDesc()).thenReturn(postContents);

        List<Ads> ads = List.of(new Ads(), new Ads(), new Ads());
        when(adsRepository.findTop3ByOrderByCreatedOnDesc()).thenReturn(ads);

        // action
        String result = homeController.Index(model, request);

        // verify
        assertEquals("index", result);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("allUsers", users);
        verify(model).addAttribute("postContents", postContents);
    }
}
