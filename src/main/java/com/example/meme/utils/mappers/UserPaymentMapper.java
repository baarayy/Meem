package com.example.meme.utils.mappers;

import com.example.meme.dto.UserPaymentDTO;
import com.example.meme.models.UserPayment;
import com.example.meme.repositories.UserPaymentRepo;
import com.example.meme.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPaymentMapper {
    private final UserPaymentRepo repo;
    private final UserRepo userRepo;


    public UserPayment toEntity(UserPaymentDTO x){
        var p = new UserPayment();
        p.setAccountNumber(x.accountNo());
        p.setPaymentProvider(x.provider());
        p.setExpiryDate(x.expiryDate());
        p.setPaymentType(x.type());
        userRepo.findById(x.userId()).ifPresent(p::setUser);
        return p;
    }

    public UserPaymentDTO toDTO(UserPayment p){
        var user = p.getUser();
        return user!=null ? new UserPaymentDTO(p.getId(),user.getId(),p.getPaymentType(),p.getPaymentProvider(),
                p.getAccountNumber(),p.getExpiryDate()
        ):null;
    }
}
