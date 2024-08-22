package com.example.meme.service;

import com.example.meme.dto.UserRegistrationRequestDTO;
import com.example.meme.dto.UserRegistrationResponseDTO;
import com.example.meme.repositories.UserRepo;
import com.example.meme.utils.mappers.AdminUserMapper;
import com.example.meme.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo repo;
    private final UserMapper mapper;
    private final AdminUserMapper adminUserMapper;

    public UserRegistrationResponseDTO registerUser(UserRegistrationRequestDTO x){
        var user = mapper.toEntity(x);
        var saved = repo.save(user);
        return mapper.toDTO(saved);
    }

    public UserRegistrationResponseDTO registerAdmin(UserRegistrationRequestDTO x){
        var user = adminUserMapper.toEntity(x);
        var saved = repo.save(user);
        return adminUserMapper.toDTO(saved);
    }
}
