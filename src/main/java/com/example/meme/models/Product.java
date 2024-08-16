package com.example.meme.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="product_name" , nullable = false , unique = true , length = 100)
    private String name;

    @Column(name = "decription" , length = 500)
    private String desc;

    @Column(name="SKU", nullable=false, unique=true, length=20)
    private String SKU;

    @ManyToOne(fetch=FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name="category_id")
    private Category category;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="inventory_id")
    private Inventory inventory;

    @Column(name="price")
    private Double price;

    @ManyToOne(fetch=FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name="discount_id")
    private Discount discount;

    @OneToMany(mappedBy="product", cascade=CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> orderItems=new ArrayList<>();


}
