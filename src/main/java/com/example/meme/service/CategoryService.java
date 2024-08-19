package com.example.meme.service;

import com.example.meme.dto.CategoryDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.repositories.CategoryRepo;
import com.example.meme.repositories.ProductRepo;
import com.example.meme.utils.mappers.CategoryMapper;
import com.example.meme.utils.specification.CategorySpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepo repo;
    private final ProductRepo productRepo;
    private final CategoryMapper mapper;
    private final CategorySpecification specification;

    @Cacheable(value = "allCategories" , key = "'findAll_' + #page + '_' + #size")
    public Page<CategoryDTO> findAll(int page,int size) {
        var pageable = PageRequest.of(page , size);
        return repo.findAll(pageable).map(mapper::toDTO);
    }

    @Cacheable(value = "categoryById" , key = "#id")
    public CategoryDTO findById(Integer id) {
        return repo.findById(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("Category with id " + id + " is not found"));
    }

    @Transactional
    @CacheEvict(value = {"allCategories" , "categoryById"} , allEntries = true)
    public CategoryDTO create(CategoryDTO x) {
        var c = mapper.toEntity(x);
        var savedCategory = repo.save(c);
        return mapper.toDTO(savedCategory);
    }

    @Transactional
    @CacheEvict(value = {"allCategories" , "categoryById"} , allEntries = true)
    public CategoryDTO update(Integer id,CategoryDTO x) {
        var c = repo.findById(id).orElseThrow(()->
                new EntityNotFoundException("Category with id " + id + " is not found"));
        c.setDesc(x.desc());
        c.setName(x.name());
        var list = x.productIds();
        if(list != null)
            c.setProducts(productRepo.findAllById(list));
        var saved = repo.save(c);
        return mapper.toDTO(saved);
    }

    @Transactional
    @CacheEvict(value = {"allCategories" , "categoryById"} , allEntries = true)
    public void delete(Integer id) {
        var optinalCategory = repo.findById(id);
        if(optinalCategory.isPresent()) {
            var category = optinalCategory.get();
            var list = category.getProducts();
            list.forEach(category::removeProduct);
            productRepo.saveAll(list);
            repo.delete(category);
        }
    }

    public Page<CategoryDTO> search(String name ,String desc ,String productName ,int page ,int size) {
        var pageable = PageRequest.of(page , size);
        var spec = Specification.where(specification.hasName(name))
                .and(specification.hasDesc(desc))
                .and(specification.hasProductsWithName(productName));
        var categories = repo.findAll(spec , pageable);
        var categoriesDTOs = categories.stream().map(mapper::toDTO).collect(Collectors.toList());
        return new PageImpl<>(categoriesDTOs , pageable , categories.getTotalElements());
    }

    @CacheEvict(value = {"allCategories" , "categoryById"} , allEntries = true)
    public void clearCache() {}
}
