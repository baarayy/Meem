package com.example.meme.utils.mappers;

import com.example.meme.dto.PaymentDetailDTO;
import com.example.meme.models.PaymentDetail;
import com.example.meme.repositories.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentDetailMapper {
    private final OrderRepo orderRepo;

    public PaymentDetail toEntity(PaymentDetailDTO x){
        var d = new PaymentDetail();
        d.setAmount(x.amount());
        d.setPaymentProvider(x.provider());
        d.setPaymentStatus(x.status());
        orderRepo.findById(x.orderId()).ifPresent(d::setOrder);
        return d;
    }

    public PaymentDetailDTO toDTO(PaymentDetail d){
        var order = d.getOrder();
        return order!=null ? new PaymentDetailDTO(d.getId(),order.getId(),d.getAmount(),
                d.getPaymentProvider(),d.getPaymentStatus()):null;
    }
}
