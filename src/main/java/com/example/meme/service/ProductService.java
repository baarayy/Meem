package com.example.meme.service;

import com.example.meme.dto.ProductDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.repositories.*;
import com.example.meme.utils.mappers.ProductMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepo repo;
    private final CategoryRepo categoryRepo;
    private final DiscountRepo discountRepo;
    private final InventoryRepo inventoryRepo;
    private final OrderItemRepo orderItemRepo;
    private final ProductMapper mapper;


    public Page<ProductDTO> findAll(int page, int size){
        var pageable = PageRequest.of(page,size);
        return repo.findAll(pageable).map(mapper::toDTO);
    }
    @Transactional
    public ProductDTO create(ProductDTO x) {
        var product = mapper.toEntity(x);
        var savedProduct = repo.save(product);
        return  mapper.toDTO(savedProduct);
    }

    @Transactional
    public ProductDTO update(Integer id ,ProductDTO x) {
        var product = repo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Product with id " + id + " is not found"));
        if(x.categoryId() != null)
            categoryRepo.findById(x.categoryId()).ifPresent(product::setCategory);
        if(x.discountId() != null)
            discountRepo.findById(x.discountId()).ifPresent(product::setDiscount);
        inventoryRepo.findById(x.inventoryId()).ifPresent(product::setInventory);
        product.setDesc(x.desc());
        product.setName(x.name());
        product.setSku(x.sku());
        product.setPrice(x.price());
        var list = x.orderItemsIds();
        if(list != null) product.setOrderItems(orderItemRepo.findAllById(list));
        var savedProduct = repo.save(product);
        return  mapper.toDTO(savedProduct);
    }

    @Transactional
    public void delete(Integer id) {
        repo.findById(id).ifPresent(repo::delete);
    }

    public List<ProductDTO> findProductWithCategoryId(Integer id) {
        return repo.findByCategoryId(id).stream().map(mapper::toDTO).collect(Collectors.toList());
    }
}
