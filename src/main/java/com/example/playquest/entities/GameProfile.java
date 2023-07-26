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

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Game getGameId() {
        return gameId;
    }

    public void setGameId(Game gameId) {
        this.gameId = gameId;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}
