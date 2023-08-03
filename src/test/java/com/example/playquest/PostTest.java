package com.example.playquest;

import com.example.playquest.controllers.Post;
import com.example.playquest.entities.*;
import com.example.playquest.repositories.*;
import com.example.playquest.services.SessionManager;
import com.example.playquest.AwsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostTest {
    @Mock
    private SessionManager sessionManager;

    @Mock
    private GameProfileRepository gameProfileRepository;

    @Mock
    private PostContentRepository postContentRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private AdsRepository adsRepository;

    @Mock
    private AwsConfig awsConfig;

    @Mock
    private Model model;

    @Mock
    private MultipartFile multipartFile1;

    @Mock
    private MultipartFile multipartFile2;


    private Post post;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        post = new Post(sessionManager, gameProfileRepository, postContentRepository, notificationRepository, usersRepository, adsRepository, awsConfig);
    }

    @Test
    public void testCreateWhenUserNotLoggedIn() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(sessionManager.isUserLoggedIn(request)).thenReturn(false);

        String result = post.Create(model, request);

        assertEquals("redirect:/login", result);
    }

    @Test
    public void testCreateWhenUserLoggedIn() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(sessionManager.isUserLoggedIn(request)).thenReturn(true);

        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(sessionManager.getUserId(request)).thenReturn(userId);
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));

        String result = post.Create(model, request);

        assertEquals("create", result);
        verify(model).addAttribute("gamesProfiles", gameProfileRepository.findAll());
        verify(model).addAttribute("user", user);
    }

    @Test
    public void testPostContentWhenUserNotLoggedIn() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(sessionManager.isUserLoggedIn(request)).thenReturn(false);

        MultipartFile[] photos = new MultipartFile[]{multipartFile1, multipartFile2};

        String result = post.postContent(model, request, "", photos, "", false, "");

        assertEquals("redirect:/login", result);
    }

}
