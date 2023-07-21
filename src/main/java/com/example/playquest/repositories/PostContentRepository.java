package com.example.playquest.repositories;

import com.example.playquest.entities.GameProfile;
import com.example.playquest.entities.PostContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostContentRepository extends JpaRepository<PostContent, Long> {
}
