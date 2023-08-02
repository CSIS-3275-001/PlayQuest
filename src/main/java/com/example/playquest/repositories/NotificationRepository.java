package com.example.playquest.repositories;

import com.example.playquest.entities.Notification;
import com.example.playquest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
