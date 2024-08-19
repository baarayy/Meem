package com.example.meme.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="category")
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="category_name",unique=true,nullable=false,length=100)
    private String name;

    @Column(name="description", length=500)
    private String desc;

    @OneToMany(mappedBy="category")
    @JsonManagedReference
    private List<Product> products=new ArrayList<>();

    public void removeProduct(Product p) {
        products.remove(p);
        p.setCategory(null);
    }
}
