package com.example.meme.utils.mappers;

import com.example.meme.dto.OrderItemDTO;
import com.example.meme.models.OrderItem;
import com.example.meme.repositories.OrderRepo;
import com.example.meme.repositories.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemMapper {
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;

    public OrderItem toEntity(OrderItemDTO x){
        var o = new OrderItem();
        o.setQuantity(x.quantity());
        productRepo.findById(x.productId()).ifPresent(o::setProduct);
        orderRepo.findById(x.orderId()).ifPresent(o::setOrder);
        return o;
    }

    public OrderItemDTO toDTO(OrderItem i){
        var p = i.getProduct();
        var o = i.getOrder();
        return (o!=null && p!=null) ? new OrderItemDTO(i.getId(),o.getId(),p.getId(),i.getQuantity()):null;
    }
}
