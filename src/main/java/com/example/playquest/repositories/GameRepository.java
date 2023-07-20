package com.example.playquest.repositories;

import com.example.playquest.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    // You can define custom query methods or use the default methods provided by JpaRepository
}