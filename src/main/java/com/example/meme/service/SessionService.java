package com.example.meme.service;

import com.example.meme.dto.SessionResponseDTO;
import com.example.meme.dto.UserShoppingSessionDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.repositories.CartItemRepo;
import com.example.meme.repositories.SessionRepo;
import com.example.meme.repositories.UserRepo;
import com.example.meme.utils.mappers.SessionMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    public Page<SessionResponseDTO> findAll(int page ,int size) {
        var p = PageRequest.of(page ,size);
        return repo.findAll(p).map(mapper::toDTO);
    }

    public SessionResponseDTO findById(Integer id) {
        return repo.findById(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("There is no user session with id " + id));
    }

    @Transactional
    public SessionResponseDTO create(UserShoppingSessionDTO x) {
        var s = mapper.toEntity(x);
        var saved = repo.save(s);
        return mapper.toDTO(saved);
    }

    @Transactional
    public SessionResponseDTO update(Integer id ,UserShoppingSessionDTO x) {
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
    public void delete(Integer id) {
        repo.findById(id).ifPresent(repo::delete);
    }

    public SessionResponseDTO getSessionByUserId(Integer id) {
        return repo.findByUserId(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("There is no user session with user id " + id));
    }
}
