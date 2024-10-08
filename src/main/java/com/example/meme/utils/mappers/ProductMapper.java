package com.example.meme.utils.mappers;

import com.example.meme.dto.ProductDTO;
import com.example.meme.models.Product;
import com.example.meme.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductMapper {
    private final CategoryRepo categoryRepo;
    private final OrderItemRepo detailRepo;
    private final DiscountRepo discountRepo;
    private final InventoryRepo inventoryRepo;

    public Product toEntity(ProductDTO x){
        var p = new Product();
        if(x.categoryId() != null) {
            var category = categoryRepo.findById(x.categoryId());
            category.ifPresent(p::setCategory);
        }
        if(x.discountId() != null) {
            var discount = discountRepo.findById(x.discountId());
            discount.ifPresent(p::setDiscount);
        }
        var inventory = inventoryRepo.findById(x.inventoryId());
        inventory.ifPresent(p::setInventory);
        var list = x.orderItemsIds();
        if(list!=null){
            p.setOrderItems(detailRepo.findAllById(list));
        }
        p.setDesc(x.desc());
        p.setName(x.name());
        p.setSku(x.sku());
        p.setPrice(x.price());
        return p;
    }

    public ProductDTO toDTO(Product p){
        var list = p.getOrderItems().stream().map(o -> o.getId()).collect(Collectors.toList());
        return (p.getCategory()!=null && p.getDiscount()!=null && p.getInventory()!=null)?
                new ProductDTO(p.getId(),p.getName(),p.getDesc(),p.getSku(),p.getPrice(),p.getCategory().getId(),
                        p.getInventory().getId(),p.getDiscount().getId(),list):null;
    }
}
