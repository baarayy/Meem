package com.example.meme.utils.mappers;

import com.example.meme.dto.UserRegistrationDTO;
import com.example.meme.models.User;
import com.example.meme.repositories.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final OrderRepo orderRepo;

    public User toEntity(UserRegistrationDTO x){
        var u = new User();
        u.setEmail(x.email());
        u.setFirstname(x.firstName());
        u.setLastname(x.lastName());
        u.setPassword(x.password());
        u.setUsername(x.username());
        u.setPhone(x.phone());
        u.setRole(x.role());
        var orderList = x.orderIds();
        if(orderList!=null){
            u.setOrders(orderRepo.findAllById(orderList));
        }
        return u;
    }

}
