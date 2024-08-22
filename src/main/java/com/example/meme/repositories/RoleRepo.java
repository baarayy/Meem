package com.example.meme.repositories;

import com.example.meme.models.Role;
import com.example.meme.utils.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(RoleEnum name);
}
