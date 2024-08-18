package com.example.meme.service;

import com.example.meme.dto.UserPaymentDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.models.UserPayment;
import com.example.meme.repositories.UserPaymentRepo;
import com.example.meme.repositories.UserRepo;
import com.example.meme.utils.PaymentProvider;
import com.example.meme.utils.PaymentType;
import com.example.meme.utils.mappers.UserPaymentMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPaymentService {
    private final UserRepo userRepo;
    private final UserPaymentRepo repo;
    private final UserPaymentMapper mapper;

    public Page<UserPaymentDTO> findAll(int page ,int size) {
        return repo.findAll(PageRequest.of(page,size)).map(mapper::toDTO);
    }

    public UserPaymentDTO findById(Integer id) {
        return repo.findById(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("There is no user payment with id " + id));
    }

    @Transactional
    public UserPaymentDTO create(UserPaymentDTO x) {
        var p = mapper.toEntity(x);
        var saved = repo.save(p);
        return mapper.toDTO(saved);
    }

    @Transactional
    public UserPaymentDTO update(Integer id ,UserPaymentDTO x) {
        var p = repo.findById(id).orElseThrow(()->
                new EntityNotFoundException("There is no user payment with id " + id));
        p.setAccountNumber(x.accountNo());
        p.setExpiryDate(x.expiryDate());
        p.setPaymentProvider(x.provider());
        p.setPaymentType(x.type());
        userRepo.findById(x.userId()).ifPresent(p::setUser);
        var saved = repo.save(p);
        return mapper.toDTO(saved);
    }

    @Transactional
    public void delete(Integer id) {
        repo.findById(id).ifPresent(repo::delete);
    }

    public UserPaymentDTO findPaymentByUserId(Integer id) {
        return repo.findByUserId(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("There is no payment with user id " + id)
        );
    }

    public List<UserPaymentDTO> findPaymentByType(PaymentType type) {
        return repo.findByPaymentType(type).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<UserPaymentDTO> findPaymentByProvider(PaymentProvider provider) {
        return repo.findByPaymentProvider(provider).stream().map(mapper::toDTO).collect(Collectors.toList());
    }
}
