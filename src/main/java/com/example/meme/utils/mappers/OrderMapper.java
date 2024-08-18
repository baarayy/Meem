package com.example.meme.utils.mappers;

import com.example.meme.dto.OrderDTO;
import com.example.meme.dto.OrderResponseDTO;
import com.example.meme.models.Order;
import com.example.meme.repositories.OrderItemRepo;
import com.example.meme.repositories.PaymentDetailRepo;
import com.example.meme.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderMapper {
    private final UserRepo userRepo;
    private final OrderItemRepo orderItemRepo;
    private final PaymentDetailRepo paymentDetailRepo;


    public Order toEntity(OrderDTO x){
        var o = new Order();
        userRepo.findById(x.userId()).ifPresent(o::setUser);
        paymentDetailRepo.findById(x.paymentDetailId()).ifPresent(o::setPaymentDetail);
        var list = x.orderItemIds();
        if(list!=null){
            var orderItems = orderItemRepo.findAllById(list);
            orderItems.forEach(o::addOrderItem);
        }
        return o;
    }

    public OrderResponseDTO toDTO(Order o){
        var list = o.getOrderItems().stream().map(x -> x.getId()).collect(Collectors.toList());
        var user = o.getUser();
        var payment = o.getPaymentDetail();
        return (user!=null && payment!=null)? new OrderResponseDTO(o.getId(),user.getId(),o.getTotal(),payment.getId(),list):null;
    }
}
