package com.example.meme.utils.mappers;

import com.example.meme.dto.UserRegistrationRequestDTO;
import com.example.meme.dto.UserRegistrationResponseDTO;
import com.example.meme.models.User;
import com.example.meme.repositories.OrderRepo;
import com.example.meme.repositories.RoleRepo;
import com.example.meme.utils.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final OrderRepo orderRepo;
    private final PasswordEncoder encoder;
    private final RoleRepo repo;

    public User toEntity(UserRegistrationRequestDTO x){
        var role = repo.findByName(RoleEnum.USER);
        var u = new User();
        u.setEmail(x.email());
        u.setFirstname(x.firstName());
        u.setLastname(x.lastName());
        u.setPassword(x.password());
        u.setUsername(x.username());
        u.setPassword(encoder.encode(x.password()));
        u.setPhone(x.phone());
        role.ifPresent(u::setRole);
        var orderList = x.orderIds();
        if(orderList!=null){
            u.setOrders(orderRepo.findAllById(orderList));
        }
        return u;
    }

    public UserRegistrationResponseDTO toDTO(User u) {
        var list = u.getOrders().stream().map(o -> o.getId()).collect(Collectors.toList());
        return new
                UserRegistrationResponseDTO(u.getId(),u.getUsername(),u.getFirstname(),u.getLastname(),u.getEmail(),u.getPhone(),u.getRole(),list);
    }
}
