package com.example.meme.service;

import com.example.meme.dto.SessionResponseDTO;
import com.example.meme.dto.UserShoppingSessionDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.repositories.CartItemRepo;
import com.example.meme.repositories.SessionRepo;
import com.example.meme.repositories.UserRepo;
import com.example.meme.utils.mappers.SessionMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final UserRepo userRepo;
    private final CartItemRepo cartItemRepo;
    private final SessionRepo repo;
    private final SessionMapper mapper;
    private Validator validator;


    @Cacheable(value="allSessions", key = "'findAll_' + #page + '_' + #size")
    public Page<SessionResponseDTO> findAll(int page ,int size) {
        var p = PageRequest.of(page ,size);
        return repo.findAll(p).map(mapper::toDTO);
    }

    @Cacheable(value="sessionById", key="#id")
    public SessionResponseDTO findById(Integer id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        return repo.findById(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("There is no user session with id " + id));
    }

    @Transactional
    @CacheEvict(value={"allSessions", "sessionById"}, allEntries=true)
    public SessionResponseDTO create(UserShoppingSessionDTO x) {
        var violations = validator.validate(x);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var s = mapper.toEntity(x);
        var saved = repo.save(s);
        return mapper.toDTO(saved);
    }

    @Transactional
    @CacheEvict(value={"allSessions", "sessionById"}, allEntries=true)
    public SessionResponseDTO update(Integer id ,UserShoppingSessionDTO x) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        var violations = validator.validate(x);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var s = repo.findById(id).orElseThrow(()->
                new EntityNotFoundException("There is no user session with id " + id));
        userRepo.findById(x.userId()).ifPresent(s::setUser);
        var list = x.cartItemIds();
        if(list != null) {
            var tmpList = cartItemRepo.findAllById(list);
            tmpList.forEach(s::addCartItem);
        }
        var saved = repo.save(s);
        return mapper.toDTO(saved);
    }

    @Transactional
    @CacheEvict(value={"allSessions", "sessionById"}, allEntries=true)
    public void delete(Integer id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        repo.findById(id).ifPresent(repo::delete);
    }

    public SessionResponseDTO findSessionByUserId(Integer id) {
        return repo.findByUserId(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("There is no user session with user id " + id));
    }

    @CacheEvict(value={"allSessions", "sessionById"}, allEntries=true)
    public void clearCache() {}
}
