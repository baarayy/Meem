package com.example.meme.service;

import com.example.meme.dto.ProductDTO;
import com.example.meme.repositories.ProductRepo;
import com.example.meme.utils.mappers.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepo repo;
    private final ProductMapper mapper;


    public Page<ProductDTO> findAll(int page, int size){
        var pageable = PageRequest.of(page,size);
        return repo.findAll(pageable).map(mapper::toDTO);
    }
}
