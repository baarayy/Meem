package com.example.meme.repositories;

import com.example.meme.models.UserAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepo extends JpaRepository<UserAddress,Integer> {
    Optional<UserAddress> findByUserId(Integer id);
    @Override
    Page<UserAddress> findAll(Pageable pageable);
}
