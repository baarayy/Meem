package com.example.meme.service;

import com.example.meme.dto.OrderItemDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.repositories.OrderItemRepo;
import com.example.meme.repositories.OrderRepo;
import com.example.meme.repositories.ProductRepo;
import com.example.meme.utils.mappers.OrderItemMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
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
    private Validator validator;

    public OrderItemDTO findById(Integer id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        return repo.findById(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("There is no order item with id " + id));
    }

    public Page<OrderItemDTO> findAll(int page ,int size) {
        return  repo.findAll(PageRequest.of(page , size)).map(mapper::toDTO);
    }

    @Transactional
    public OrderItemDTO create(OrderItemDTO x) {
        var violations = validator.validate(x);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var o = mapper.toEntity(x);
        var saved = repo.save(o);
        return mapper.toDTO(saved);
    }

    @Transactional
    public OrderItemDTO update(Integer id , OrderItemDTO x) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        var violations = validator.validate(x);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
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
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        repo.findById(id).ifPresent(repo::delete);
    }

    public List<OrderItemDTO> findOrderDetailsForProduct(Integer id) {
        return repo.findByProductId(id).stream().map(mapper::toDTO).collect(Collectors.toList());
    }
}
