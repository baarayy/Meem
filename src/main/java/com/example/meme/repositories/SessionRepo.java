package com.example.meme.repositories;

import com.example.meme.models.UserShoppingSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<UserShoppingSession,Integer> {
    Optional<UserShoppingSession> findByUserId(Integer id);
    List<UserShoppingSession> findByTotalBetween(Double min,Double max);
    @Override
    Page<UserShoppingSession> findAll(Pageable pageable);
}
