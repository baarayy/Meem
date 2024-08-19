package com.example.meme.service;

import com.example.meme.dto.OrderDTO;
import com.example.meme.dto.OrderResponseDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.models.Order;
import com.example.meme.repositories.OrderItemRepo;
import com.example.meme.repositories.OrderRepo;
import com.example.meme.repositories.PaymentDetailRepo;
import com.example.meme.repositories.UserRepo;
import com.example.meme.utils.mappers.OrderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo repo;
    private final UserRepo userRepo;
    private final OrderItemRepo orderItemRepo;
    private final PaymentDetailRepo paymentDetailRepo;
    private final OrderMapper mapper;

    public Page<OrderResponseDTO> findAll(int page , int size) {
        var pageable = PageRequest.of(page , size);
        return repo.findAll(pageable).map(mapper::toDTO);
    }

    public OrderResponseDTO findById(Integer id) {
        return repo.findById(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("There is no order with id " + id));
    }
    @Transactional
    public OrderResponseDTO create(OrderDTO x){
        var o = mapper.toEntity(x);
        var saved = repo.save(o);
        return mapper.toDTO(saved);
    }

    @Transactional
    public OrderResponseDTO update(Integer id,OrderDTO x){
        var o = repo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Order with id: " + id + " isn't found" ));
        var list = x.orderItemIds();
        if(list!=null){
            var itemList = orderItemRepo.findAllById(list);
            itemList.forEach(o::addOrderItem);
            repo.save(o);
            orderItemRepo.saveAll(itemList);
        }
        paymentDetailRepo.findById(x.paymentDetailId()).ifPresent(o::setPaymentDetail);
        userRepo.findById(x.userId()).ifPresent(o::setUser);
        var saved  = repo.save(o);
        return mapper.toDTO(saved);
    }

    @Transactional
    public void delete(Integer id){
        repo.findById(id).ifPresent(repo::delete);
    }

    public List<OrderResponseDTO> findOrdersByUser(Integer id){
        return repo.findByUserId(id).stream().map(mapper::toDTO).collect(Collectors.toList());
    }
}
