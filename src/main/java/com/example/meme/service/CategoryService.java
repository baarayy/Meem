package com.example.meme.service;

import com.example.meme.dto.CategoryDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.repositories.CategoryRepo;
import com.example.meme.repositories.ProductRepo;
import com.example.meme.utils.mappers.CategoryMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepo repo;
    private final ProductRepo productRepo;
    private final CategoryMapper mapper;

    public Page<CategoryDTO> findAll(int page,int size) {
        var pageable = PageRequest.of(page , size);
        return repo.findAll(pageable).map(mapper::toDTO);
    }

    public CategoryDTO findById(Integer id) {
        return repo.findById(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("Category with id " + id + " is not found"));
    }

    @Transactional
    public CategoryDTO create(CategoryDTO x) {
        var c = mapper.toEntity(x);
        var savedCategory = repo.save(c);
        return mapper.toDTO(savedCategory);
    }

    @Transactional
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
    public void delete(Integer id) {
        repo.findById(id).ifPresent(repo::delete);
    }
}
