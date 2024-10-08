package com.example.meme.repositories;

import com.example.meme.models.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem,Integer> {
    @Override
    Page<CartItem> findAll(Pageable pageable);
}
