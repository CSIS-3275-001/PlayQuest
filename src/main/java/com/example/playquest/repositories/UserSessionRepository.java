package com.example.playquest.repositories;

import com.example.playquest.entities.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, String> {
    Optional<UserSession> findBySessionId(String sessionId);

    void deleteBySessionId(String sessionId);
}
