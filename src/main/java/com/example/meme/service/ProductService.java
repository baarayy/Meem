package com.example.meme.service;

import com.example.meme.dto.ProductDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.repositories.*;
import com.example.meme.utils.mappers.ProductMapper;
import com.example.meme.utils.specification.ProductSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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
    private final ProductSpecification specification;
    private final ProductMapper mapper;

    @Cacheable(value = "allProducts" , key="'findAll_' + #page + '_' + #size")
    public Page<ProductDTO> findAll(int page, int size){
        var pageable = PageRequest.of(page,size);
        return repo.findAll(pageable).map(mapper::toDTO);
    }

    @Cacheable(value="productById", key="#id")
    public ProductDTO findById(Integer id){
        var opProduct = repo.findById(id);
        return opProduct.map(mapper::toDTO).orElseThrow(
                () -> new EntityNotFoundException("There is no product with id " + id));
    }

    @Transactional
    @CacheEvict(value={"allProducts", "productById"}, allEntries=true)
    public ProductDTO create(ProductDTO x) {
        var product = mapper.toEntity(x);
        var savedProduct = repo.save(product);
        return  mapper.toDTO(savedProduct);
    }

    @Transactional
    @CacheEvict(value={"allProducts", "productById"}, allEntries=true)
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
    @CacheEvict(value={"allProducts", "productById"}, allEntries=true)
    public void delete(Integer id) {
        repo.findById(id).ifPresent(repo::delete);
    }

    public List<ProductDTO> findProductWithCategoryId(Integer id) {
        return repo.findByCategoryId(id).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public Page<ProductDTO> search(String name,String desc,Boolean discountStatus,String categoryName,
                                   Double minPrice,Double maxPrice,int page ,int size) {
        var pageable = PageRequest.of(page , size);
        var spec = Specification.where(specification.hasName(name)
                .and(specification.hasCategoryName(categoryName))
                .and(specification.hasDesc(desc))
                .and(specification.hasDiscountStatus(discountStatus))
                .and(specification.hasPriceBetween(minPrice,maxPrice)));

        var products = repo.findAll(spec,pageable);
        var productsDTO = products.stream().map(mapper::toDTO).collect(Collectors.toList());
        return new PageImpl<>(productsDTO,pageable,products.getTotalElements());
    }
}
