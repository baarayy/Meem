package com.example.meme.utils.mappers;

import com.example.meme.dto.OrderDTO;
import com.example.meme.models.Order;
import com.example.meme.repositories.OrderItemRepo;
import com.example.meme.repositories.OrderRepo;
import com.example.meme.repositories.PaymentDetailRepo;
import com.example.meme.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderRepo repo;
    private final UserRepo userRepo;
    private final OrderItemRepo detailRepo;
    private final PaymentDetailRepo paymentRepo;


    public Order toEntity(OrderDTO x){
        var o = new Order();
        o.setTotal(x.total());
        userRepo.findById(x.userId()).ifPresent(o::setUser);
        paymentRepo.findById(x.paymentDetailId()).ifPresent(o::setPaymentDetail);
        var list = x.orderItemIds();
        if(list!=null){
            o.setOrderItems(detailRepo.findAllById(list));
        }
        return o;
    }

    public OrderDTO toDTO(Order o){
        var list = o.getOrderItems().stream().map(x -> x.getId()).collect(Collectors.toList());
        var user = o.getUser();
        var payment = o.getPaymentDetail();
        return (user!=null && payment!=null)? new OrderDTO(o.getId(),user.getId(),o.getTotal(),payment.getId(),list):null;
    }
}
