package com.example.playquest.repositories;

import com.example.playquest.entities.Notification;
import com.example.playquest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiver(User receiver);
}
