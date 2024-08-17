package com.example.meme.utils.mappers;

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
    private final SessionRepo repo;
    private final UserRepo urepo;
    private final CartItemRepo crepo;

    public UserShoppingSessionDTO toDTO(UserShoppingSession s){
        var list = s.getCartItems().stream().map(x -> x.getId()).collect(Collectors.toList());
        var user = s.getUser();
        return user!=null ? new UserShoppingSessionDTO(s.getId(),user.getId(),s.getTotal(),list):null;
    }

    public UserShoppingSession toEntity(UserShoppingSessionDTO x){
        var s = new UserShoppingSession();
        s.setTotal(x.total());
        urepo.findById(x.userId()).ifPresent(s::setUser);
        var list = x.cartItemIds();
        if(list!=null){
            s.setCartItems(crepo.findAllById(list));
        }
        return s;
    }
}
