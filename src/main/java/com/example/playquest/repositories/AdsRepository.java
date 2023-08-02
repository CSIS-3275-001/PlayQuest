package com.example.playquest.repositories;

import com.example.playquest.entities.Ads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdsRepository extends JpaRepository<Ads, Long> {
    List<Ads> findTop3ByOrderByCreatedOnDesc();
}
