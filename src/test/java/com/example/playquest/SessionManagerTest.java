package com.example.playquest;

import com.example.playquest.entities.UserSession;
import com.example.playquest.services.SessionManager;
import com.example.playquest.repositories.UserSessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionManagerTest {
    @Mock
    private UserSessionRepository sessionRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private SessionManager sessionManager;

    @Test
    void createSessionTest() {
        String sessionId = "testSession";
        Long userId = 1L;
        LocalDateTime expirationTime = LocalDateTime.now();

        sessionManager.createSession(sessionId, userId, expirationTime, response);

        verify(sessionRepository, times(1)).save(any(UserSession.class));
        verify(response, times(1)).addCookie(any(Cookie.class));
    }

    @Test
    void getSessionTest() {
        String sessionId = "testSession";
        UserSession session = new UserSession();
        session.setSessionId(sessionId);
        session.setUserId(1L);
        session.setExpirationTime(LocalDateTime.now());
        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.of(session));

        Optional<UserSession> result = sessionManager.getSession(sessionId);

        assertTrue(result.isPresent());
        assertEquals(sessionId, result.get().getSessionId());
    }

    @Test
    void deleteSessionTest() {
        String sessionId = "testSession";

        sessionManager.deleteSession(sessionId);

        verify(sessionRepository, times(1)).deleteBySessionId(sessionId);
    }

    @Test
    void isUserLoggedInTest() {
        String sessionId = "testSession";
        UserSession session = new UserSession();
        session.setSessionId(sessionId);
        session.setUserId(1L);
        session.setExpirationTime(LocalDateTime.now());
        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.of(session));
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("sessionId", sessionId)});

        assertTrue(sessionManager.isUserLoggedIn(request));
    }

}
