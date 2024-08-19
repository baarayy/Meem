package com.example.meme.utils.mappers;

import com.example.meme.dto.SessionResponseDTO;
import com.example.meme.dto.UserShoppingSessionDTO;
import com.example.meme.models.UserShoppingSession;
import com.example.meme.repositories.CartItemRepo;
import com.example.meme.repositories.SessionRepo;
import com.example.meme.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionMapper {
    private final UserRepo userRepo;
    private final SessionRepo repo;
    private final CartItemRepo cartItemRepo;

    public SessionResponseDTO toDTO(UserShoppingSession s){
        var list = s.getCartItems().stream().map(x -> x.getId()).collect(Collectors.toList());
        var user = s.getUser();
        return user!=null ? new SessionResponseDTO(s.getId(),user.getId(),s.getTotal(),list):null;
    }

    public UserShoppingSession toEntity(UserShoppingSessionDTO x){
        var s = new UserShoppingSession();
        userRepo.findById(x.userId()).ifPresent(s::setUser);
        var list = x.cartItemIds();
        if(list!=null){
            var cartlist = cartItemRepo.findAllById(list);
            cartlist.forEach(s::addCartItem);
            cartItemRepo.saveAll(cartlist);
            repo.save(s);
        }
        return s;
    }
}
