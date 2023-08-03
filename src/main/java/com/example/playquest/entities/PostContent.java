package com.example.playquest.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
public class PostContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ElementCollection
    @Column(name = "file_name")
    private List<String> images;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private boolean toggleStatus;

    @Column(nullable = false)
    private String spinnerSelection;

    @Column(nullable = false)
    private int likes;


    @JoinColumn(name = "profile_id")
    @ManyToOne
    private GameProfile profileId;

    public GameProfile getProfileId() {
        return profileId;
    }

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private LocalDateTime updatedOn;

    @PrePersist
    @PreUpdate
    public void updateTimestamps() {
        updatedOn = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public void setUserId(Long id) {
        this.user = new User();
        this.user.setId(id);
    }

    // Constructors
    public PostContent() {
    }

    public PostContent(String title, List<String> images, String description, boolean toggleStatus, String spinnerSelection) {
        this.title = title;
        this.images = images;
        this.description = description;
        this.toggleStatus = toggleStatus;
        this.spinnerSelection = spinnerSelection;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isToggleStatus() {
        return toggleStatus;
    }

    public void setToggleStatus(boolean toggleStatus) {
        this.toggleStatus = toggleStatus;
    }

    public String getSpinnerSelection() {
        return spinnerSelection;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setSpinnerSelection(String spinnerSelection) {
        this.spinnerSelection = spinnerSelection;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
}
