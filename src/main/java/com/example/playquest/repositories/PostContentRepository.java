package com.example.playquest.repositories;

import com.example.playquest.entities.PostContent;
import com.example.playquest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostContentRepository extends JpaRepository<PostContent, Long> {

    Optional<PostContent> findById(Long id);

    List<PostContent> findByUser(User user);

    List<PostContent> findAllByOrderByCreatedOnDesc();

    void deleteById(Long id);
}
