package com.example.meme.repositories;

import com.example.meme.models.UserPayment;
import com.example.meme.utils.PaymentProvider;
import com.example.meme.utils.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPaymentRepo extends JpaRepository<UserPayment,Integer>, JpaSpecificationExecutor<UserPayment> {
    List<UserPayment> findByPaymentProvider(PaymentProvider provider);
    List<UserPayment> findByPaymentType(PaymentType type);
    Optional<UserPayment> findByUserId(Integer id);
    @Override
    Page<UserPayment> findAll(Pageable pageable);
}
