package com.example.playquest;

import com.example.playquest.controllers.Login;
import com.example.playquest.entities.User;
import com.example.playquest.repositories.UsersRepository;
import com.example.playquest.services.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class LoginTest {
    @InjectMocks
    private Login loginController;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private SessionManager sessionManager;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testLoginSuccess() {
        String email = "test@example.com";
        String password = "testpass";
        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setPassword(password);

        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(user));
        doNothing().when(sessionManager).createSession(anyString(), eq(user.getId()), any(LocalDateTime.class), eq(response));


        RedirectView result = loginController.login(email, password, response);
        assertEquals("/", result.getUrl());
    }

    @Test
    public void testLoginFailure() {
        String email = "test@example.com";
        String password = "wrongpass";

        when(usersRepository.findByEmail(email)).thenReturn(Optional.empty());

        RedirectView result = loginController.login(email, password, response);
        assertEquals("/login?error", result.getUrl());
    }

    @Test
    public void testLogoutSuccess() {
        String sessionId = "sessionId";
        when(sessionManager.getSessionIdFromCookie(request)).thenReturn(sessionId);
        doNothing().when(sessionManager).deleteSession(sessionId);

        String result = loginController.logout(request, response);
        assertEquals("redirect:/login", result);
    }

}
