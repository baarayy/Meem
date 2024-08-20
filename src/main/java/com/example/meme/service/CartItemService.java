package com.example.meme.service;

import com.example.meme.dto.CartItemDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.repositories.CartItemRepo;
import com.example.meme.repositories.ProductRepo;
import com.example.meme.repositories.SessionRepo;
import com.example.meme.utils.mappers.CartItemMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Data
public class CartItemService {
    private final CartItemRepo repo;
    private final SessionRepo sessionRepo;
    private final ProductRepo productRepo;
    private final CartItemMapper mapper;
    private Validator validator;

    @Transactional
    @CacheEvict(value={"allCartItems", "cartItemById"}, allEntries=true)
    public CartItemDTO create(CartItemDTO x) {
        var violations = validator.validate(x);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var cart = mapper.toEntity(x);
        var saved = repo.save(cart);
        return mapper.toDTO(saved);
    }

    @Cacheable(value="allCartItems", key = "'findAll_' + #page + '_' + #size")
    public Page<CartItemDTO> findAll(int page ,int size) {
        return repo.findAll(PageRequest.of(page , size)).map(mapper::toDTO);
    }

    @Cacheable(value="cartItemById", key="#id")
    public CartItemDTO findById(Integer id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        return repo.findById(id).map(mapper::toDTO).orElseThrow(() ->
                new EntityNotFoundException("There is no cart item with id " + id));
    }

    @Transactional
    @CacheEvict(value={"allCartItems", "cartItemById"}, allEntries=true)
    public CartItemDTO update(Integer id ,CartItemDTO x) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        var violations = validator.validate(x);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var o = repo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("There is no cart item with id " + id));
        o.setQuantity(x.quantity());
        productRepo.findById(x.productId()).ifPresent(o::setProduct);
        sessionRepo.findById(x.sessionId()).ifPresent(o::setSession);
        var saved = repo.save(o);
        return mapper.toDTO(saved);
    }

    @Transactional
    @CacheEvict(value={"allCartItems", "cartItemById"}, allEntries=true)
    public void delete(Integer id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        repo.findById(id).ifPresent(repo::delete);
    }
}
