package com.example.meme.repositories;

import com.example.meme.models.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order,Integer> {
    List<Order>findByTotalBetween(Double min,Double max);
    List<Order>findByUserId(Integer id);
}
