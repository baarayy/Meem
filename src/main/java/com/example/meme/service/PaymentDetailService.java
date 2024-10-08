package com.example.meme.service;

import com.example.meme.dto.PaymentDetailDTO;
import com.example.meme.dto.PaymentDetailResponseDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.models.PaymentDetail;
import com.example.meme.repositories.OrderRepo;
import com.example.meme.repositories.PaymentDetailRepo;
import com.example.meme.utils.PaymentStatus;
import com.example.meme.utils.mappers.PaymentDetailMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentDetailService {
    private final OrderRepo orderRepo;
    private final PaymentDetailRepo repo;
    private final PaymentDetailMapper mapper;
    private Validator validator;


    @Cacheable(value="allPaymentDetails", key = "'findAll_' + #page + '_' + #size")
    public Page<PaymentDetailResponseDTO> findAll(int page ,int size) {
        return repo.findAll(PageRequest.of(page , size)).map(mapper::toDTO);
    }

    @Cacheable(value="paymentDetailById", key="#id")
    public PaymentDetailResponseDTO findById(Integer id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        return repo.findById(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("There is no payment detail with id " + id));
    }
    @Transactional
    @CacheEvict(value={"allPaymentDetails", "paymentDetailById"}, allEntries=true)
    public PaymentDetailResponseDTO create(PaymentDetailDTO x){
        var violations = validator.validate(x);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var p = mapper.toEntity(x);
        var saved = repo.save(p);
        return mapper.toDTO(saved);
    }

    @Transactional
    @CacheEvict(value={"allPaymentDetails", "paymentDetailById"}, allEntries=true)
    public PaymentDetailResponseDTO update(Integer id, PaymentDetailDTO x){
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        var violations = validator.validate(x);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var p = repo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Payment Detail with id: " + id + " isn't found")
        );
        orderRepo.findById(x.orderId()).ifPresent(p::setOrder);
        p.setPaymentProvider(x.provider());
        p.setPaymentStatus(x.status());
        var saved = repo.save(p);
        return mapper.toDTO(saved);
    }

    @Transactional
    @CacheEvict(value={"allPaymentDetails", "paymentDetailById"}, allEntries=true)
    public void delete(Integer id){
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        repo.findById(id).ifPresent(repo::delete);
    }

    public List<PaymentDetailResponseDTO> findByPaymentStatus(PaymentStatus status){
        return repo.findByPaymentStatus(status).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<PaymentDetailResponseDTO> findPaymentsWithAmountGreaterThan(Double amount){
        return repo.findByAmountGreaterThanEqual(amount).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @CacheEvict(value={"allPaymentDetails", "paymentDetailById"}, allEntries=true)
    public void clearCache() {}
}
