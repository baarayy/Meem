package com.example.meme.security.service;

import com.example.meme.dto.UserRegistrationRequestDTO;
import com.example.meme.repositories.UserRepo;
import com.example.meme.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepo repo;
    private final UserService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repo.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException( "User Details with username: " + username + " isn't found!"));
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().name()));
        return new User(username, user.getPassword(),authorities);
    }
}
