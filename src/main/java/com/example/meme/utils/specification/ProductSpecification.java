package com.example.meme.utils.specification;

import com.example.meme.models.Category;
import com.example.meme.models.Discount;
import com.example.meme.models.Product;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProductSpecification {
    public Specification<Product> hasName(String name) {
        return (root , query , criteriaBuilder) -> {
            if(name == null || name.isBlank())
                return criteriaBuilder.conjunction();
            return criteriaBuilder.like(root.get(name) , "%" + name + "%");
        };
    }

    public Specification<Product> hasDesc(String desc){
        return (root, query, criteriaBuilder) -> {
            if(desc == null || desc.isBlank()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("desc"), "%" + desc+ "%");
        };
    }

    public Specification<Product> hasCategoryName(String categoryName){
        return (root, query, criteriaBuilder) -> {
            if(categoryName == null || categoryName.isBlank()){
                return criteriaBuilder.conjunction();
            }
            Join<Product, Category> categoryJoin = root.join("category");
            return criteriaBuilder.equal(categoryJoin.get("name"), categoryName);
        };
    }

    public Specification<Product> hasDiscountStatus(Boolean status){
        return (root, query, criteriaBuilder) -> {
            if(status==null){
                return criteriaBuilder.conjunction();
            }
            Join<Product, Discount> discountJoin = root.join("discount");
            return criteriaBuilder.equal(discountJoin.get("active"), status);
        };
    }

    public Specification<Product> hasPriceBetween(Double minPrice, Double maxPrice){
        return (root, query, criteriaBuilder) -> {
            if(minPrice == null && maxPrice == null){
                return criteriaBuilder.conjunction();
            }
            if(maxPrice == null){
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            }
            if(minPrice == null){
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }

            return criteriaBuilder.between(root.get("price"),minPrice, maxPrice);
        };
    }
}
