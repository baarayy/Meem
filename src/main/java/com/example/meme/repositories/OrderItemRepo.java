package com.example.meme.repositories;

import com.example.meme.models.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem,Integer> {
    List<OrderItem> findByProductId(Integer id);
    @Override
    Page<OrderItem> findAll(Pageable pageable);
}
