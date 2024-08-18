package com.example.meme.service;

import com.example.meme.dto.OrderItemDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.models.OrderItem;
import com.example.meme.repositories.OrderItemRepo;
import com.example.meme.repositories.OrderRepo;
import com.example.meme.repositories.ProductRepo;
import com.example.meme.utils.mappers.OrderItemMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepo repo;
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final OrderItemMapper mapper;

    public OrderItemDTO findById(Integer id) {
        return repo.findById(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("There is no order item with id " + id));
    }

    public Page<OrderItemDTO> findAll(int page ,int size) {
        return  repo.findAll(PageRequest.of(page , size)).map(mapper::toDTO);
    }

    @Transactional
    public OrderItemDTO create(OrderItemDTO x) {
        var o = mapper.toEntity(x);
        var saved = repo.save(o);
        return mapper.toDTO(saved);
    }

    @Transactional
    public OrderItemDTO update(Integer id , OrderItemDTO x) {
        var o = repo.findById(id).orElseThrow(()->
                new EntityNotFoundException("There is no order item with id " + id));
        o.setQuantity(x.quantity());
        productRepo.findById(x.productId()).ifPresent(o::setProduct);
        orderRepo.findById(x.orderId()).ifPresent(o::setOrder);
        var saved = repo.save(o);
        return mapper.toDTO(saved);
    }

    @Transactional
    public void delete(Integer id) {
        repo.findById(id).ifPresent(repo::delete);
    }

    public List<OrderItemDTO> findOrderDetailsForProduct(Integer id) {
        return repo.findByProductId(id).stream().map(mapper::toDTO).collect(Collectors.toList());
    }
}
