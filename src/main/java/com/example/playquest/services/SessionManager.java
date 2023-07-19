package com.example.playquest.services;

import com.example.playquest.entities.UserSession;
import com.example.playquest.repositories.UserSessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SessionManager {
    private final UserSessionRepository sessionRepository;

    public SessionManager(UserSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void createSession(String sessionId, Long userId, LocalDateTime expirationTime, HttpServletResponse response) {
        UserSession session = new UserSession();
        session.setSessionId(sessionId);
        session.setUserId(userId);
        session.setExpirationTime(expirationTime);

        sessionRepository.save(session);

        // Set the session ID as a cookie in the response
        Cookie sessionCookie = new Cookie("sessionId", sessionId);
        sessionCookie.setMaxAge(-1); // Set the cookie to be valid for the duration of the browser session
        sessionCookie.setPath("/"); // Set the cookie to be accessible across the entire application
        response.addCookie(sessionCookie);
    }

    public Optional<UserSession> getSession(String sessionId) {
        return sessionRepository.findBySessionId(sessionId);
    }

    @Transactional
    public void deleteSession(String sessionId) {
        sessionRepository.deleteBySessionId(sessionId);
    }

    public boolean isUserLoggedIn(HttpServletRequest request) {
        // Retrieve the session ID from the cookie
        String sessionId = getSessionIdFromCookie(request);

        if (sessionId != null) {
            // Retrieve the session based on the session ID
            Optional<UserSession> sessionOptional = getSession(sessionId);

            // Check if the session is present and if it indicates the user is logged in
            return sessionOptional.isPresent() && sessionOptional.get().getUserId() != null;
        }

        return false;
    }

    public String getSessionIdFromCookie(HttpServletRequest request) {
        // Retrieve the cookies from the request
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            // Loop through the cookies and find the session ID cookie
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionId")) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    public void deleteSessionIdCookie(HttpServletResponse response) {
        Cookie sessionCookie = new Cookie("sessionId", null);
        sessionCookie.setMaxAge(0); // Set the cookie expiration to 0 to delete it
        sessionCookie.setPath("/"); // Set the cookie path to match the one used during creation
        response.addCookie(sessionCookie);
    }
}

