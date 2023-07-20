package com.example.playquest.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class GameProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game gameId;
    private LocalDateTime createdOn;
    private boolean onSale;
    private double amount;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    // Constructors, getters, and setters

    public Game getGame() {
        return gameId;
    }

    public User getUser() {
        return userId;
    }

}
