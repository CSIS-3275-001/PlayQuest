package com.example.playquest.entities;

import jakarta.persistence.*;

@Entity

public class Notification {
    @Id
    @GeneratedValue
    private Long notification_id;

    private String notification_message;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender_id;

    public String getNotification_message() {
        return notification_message;
    }

    public void setNotification_message(String notification_message) {
        this.notification_message = notification_message;
    }

    public User getSender_id() {
        return sender_id;
    }

    public void setSender_id(User sender_id) {
        this.sender_id = sender_id;
    }

    public User getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(User receiver_id) {
        this.receiver_id = receiver_id;
    }

    @ManyToOne
    @JoinColumn(name = "user_id",insertable=false, updatable=false)
    private User receiver_id;


    public void setNotification_id(Long notificationId) {
        this.notification_id = notificationId;
    }

    public Long getNotification_id() {
        return notification_id;
    }
}
