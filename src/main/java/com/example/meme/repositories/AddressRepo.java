package com.example.meme.repositories;

import com.example.meme.models.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepo extends JpaRepository<UserAddress,Integer> {
    Optional<UserAddress> findByUserId(Integer id);
}
