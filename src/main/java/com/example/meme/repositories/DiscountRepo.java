package com.example.meme.repositories;

import com.example.meme.models.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepo extends JpaRepository<Discount,Integer>, JpaSpecificationExecutor<Discount> {

    @Override
    Page<Discount> findAll(Pageable pageable);
}
