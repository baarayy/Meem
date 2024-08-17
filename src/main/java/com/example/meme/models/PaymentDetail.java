package com.example.meme.models;

import com.example.meme.utils.PaymentProvider;
import com.example.meme.utils.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name="payment_detail")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor

public class PaymentDetail extends BaseEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @OneToOne(mappedBy="paymentDetail")
    private Order order;

    @Column(name="amount",nullable=false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name="payment_provider",nullable=false)
    private PaymentProvider paymentProvider;

    @Enumerated(EnumType.STRING)
    @Column(name="payment_status",nullable=false)
    private PaymentStatus paymentStatus;
}
