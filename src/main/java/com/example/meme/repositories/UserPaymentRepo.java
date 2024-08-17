package com.example.meme.repositories;

import com.example.meme.models.UserPayment;
import com.example.meme.utils.PaymentProvider;
import com.example.meme.utils.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPaymentRepo extends JpaRepository<UserPayment,Integer> {
    List<UserPayment> findByPaymentProvider(PaymentProvider provider);
    List<UserPayment> findByPaymentType(PaymentType type);
    List<UserPayment> findByUserId(Integer id);
}
