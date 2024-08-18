package com.example.meme.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="shopping_session")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserShoppingSession extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    @Transient
    private Double total;

    @OneToMany(mappedBy = "session" , cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonBackReference
    private List<CartItem> cartItems = new ArrayList<>();

    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItem.setSession(null);
    }
    public void addCartItem(CartItem c){
        cartItems.add(c);
        c.setSession(this);
        this.recalculateTotal();
    }

    private void recalculateTotal() {
        this.total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
}
