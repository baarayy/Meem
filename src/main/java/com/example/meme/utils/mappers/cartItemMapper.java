package com.example.meme.utils.mappers;

import com.example.meme.dto.CartItemDTO;
import com.example.meme.models.CartItem;
import com.example.meme.repositories.ProductRepo;
import com.example.meme.repositories.SessionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class cartItemMapper {
    private final SessionRepo sessionRepo;
    private final ProductRepo productRepo;

    public CartItem toEntity(CartItemDTO x) {
        var c = new CartItem();
        sessionRepo.findById(x.sessionId()).ifPresent(c::setSession);
        productRepo.findById(x.productId()).ifPresent(c::setProduct);
        c.setQuantity(x.quantity());
        return c;
    }

    public CartItemDTO toDTO(CartItem c){
        return (c.getProduct()!=null && c.getSession()!=null) ?
                new CartItemDTO(c.getId(),c.getSession().getId(),c.getProduct().getId(),c.getQuantity()) : null;
    }
}
