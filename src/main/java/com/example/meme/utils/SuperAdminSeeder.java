package com.example.meme.utils;

import com.example.meme.models.User;
import com.example.meme.repositories.RoleRepo;
import com.example.meme.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SuperAdminSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder encoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createSuperAdmin();
    }

    public void createSuperAdmin() {
        var role = roleRepo.findByName(RoleEnum.SUPERADMIN);
        if(!userRepo.existsByUsername("baraa")) {
            var user = new User();
            user.setUsername("baraa");
            user.setPassword(encoder.encode("baraa"));
            user.setEmail("albaroobaraa@gmail.com");
            user.setFirstname("Albaraa");
            user.setLastname("Ahmed");
            user.setRole(role.get());
            user.setPhone("0123456789");
            userRepo.save(user);
        }
    }
}
