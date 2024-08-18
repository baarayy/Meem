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
import lombok.RequiredArgsConstructor;
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

    public Page<PaymentDetailResponseDTO> findAll(int page ,int size) {
        return repo.findAll(PageRequest.of(page , size)).map(mapper::toDTO);
    }

    public PaymentDetailResponseDTO findById(Integer id) {
        return repo.findById(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("There is no payment detail with id " + id));
    }
    @Transactional
    public PaymentDetailResponseDTO create(PaymentDetailDTO x){
        var p = mapper.toEntity(x);
        var saved = repo.save(p);
        return mapper.toDTO(saved);
    }

    @Transactional
    public PaymentDetailResponseDTO update(Integer id, PaymentDetailDTO x){
        var p  =repo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Payment Detail with id: " + id + " isn't found")
        );
        orderRepo.findById(x.orderId()).ifPresent(p::setOrder);
        p.setPaymentProvider(x.provider());
        p.setPaymentStatus(x.status());
        var saved = repo.save(p);
        return mapper.toDTO(saved);
    }

    @Transactional
    public void delete(Integer id){
        repo.findById(id).ifPresent(repo::delete);
    }

    public List<PaymentDetailResponseDTO> findByPaymentStatus(PaymentStatus status){
        return repo.findByPaymentStatus(status).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<PaymentDetailResponseDTO> findPaymentsWithAmountGreaterThan(Double amount){
        return repo.findByAmountGreaterThanEqual(amount).stream().map(mapper::toDTO).collect(Collectors.toList());
    }
}
