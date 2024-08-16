package com.example.meme.models;

import com.example.meme.utils.PaymentProvider;
import com.example.meme.utils.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="user_payment")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserPayment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name="payment_type")
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name="payment_provider")
    private PaymentProvider paymentProvider;

    @Column(name="account_no")
    private Integer accountNumber;

    @Column(name="expiry")
    private LocalDate expiryDate;
}
