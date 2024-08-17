package com.example.meme.repositories;

import com.example.meme.models.PaymentDetail;
import com.example.meme.utils.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDetailRepo extends JpaRepository<PaymentDetail,Integer> {
    List<PaymentDetail> findByPaymentStatus(PaymentStatus status);
    List<PaymentDetail> findByAmountGreaterThanEqual(Double amount);
    @Override
    Page<PaymentDetail> findAll(Pageable pageable);
}
