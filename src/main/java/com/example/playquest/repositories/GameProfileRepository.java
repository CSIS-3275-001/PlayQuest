package com.example.playquest.repositories;

import com.example.playquest.entities.GameProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameProfileRepository extends JpaRepository<GameProfile, Long> {
    // Additional custom query methods can be defined here if needed
}
