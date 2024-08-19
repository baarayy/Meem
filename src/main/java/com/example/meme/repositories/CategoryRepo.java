package com.example.meme.repositories;

import com.example.meme.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Integer>, JpaSpecificationExecutor<Category> {
    @Override
    Page<Category> findAll(Pageable pageable);
}
