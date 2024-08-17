package com.example.meme.utils.mappers;

import com.example.meme.dto.CategoryDTO;
import com.example.meme.models.Category;
import com.example.meme.repositories.CategoryRepo;
import com.example.meme.repositories.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryMapper {
    private final CategoryRepo repo;
    private final ProductRepo productRepo;

    public Category toEntity(CategoryDTO x) {
        var c = new Category();
        c.setDesc(x.desc());
        c.setName(x.name());
        var list = x.productIds();
        if(list != null) c.setProducts(productRepo.findAllById(list));
        return c;
    }

    public CategoryDTO ToDTO(Category c){
        var list = c.getProducts().stream().map(p -> p.getId()).collect(Collectors.toList());
        return new CategoryDTO(c.getId(),c.getName(),c.getDesc(),list);
    }
}
