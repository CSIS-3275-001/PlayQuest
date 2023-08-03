package com.example.playquest.repositories;

import com.example.playquest.entities.GameProfile;
import com.example.playquest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameProfileRepository extends JpaRepository<GameProfile, Long> {
    List<GameProfile> findByUserId(User user);
    // Additional custom query methods can be defined here if needed
}
