package com.example.meme.service;

import com.example.meme.dto.UserLoginRequestDTO;
import com.example.meme.dto.UserLoginResponseDTO;
import com.example.meme.dto.UserRegistrationRequestDTO;
import com.example.meme.dto.UserRegistrationResponseDTO;
import com.example.meme.repositories.UserRepo;
import com.example.meme.security.service.JwtService;
import com.example.meme.utils.mappers.AdminUserMapper;
import com.example.meme.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo repo;
    private final UserMapper mapper;
    private final AdminUserMapper adminUserMapper;
    private final AuthenticationManager manager;
    private final JwtService jwtService;

    public UserLoginResponseDTO loginUser(UserLoginRequestDTO x){
        try {
            Authentication authentication = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(x.username(), x.password())
            );

            String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());

            UserLoginResponseDTO response = new UserLoginResponseDTO();
            response.setToken(token);
            response.setExpiresIn(jwtService.getJwtExpiration());

            return response;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

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
