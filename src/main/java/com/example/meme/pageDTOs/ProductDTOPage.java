package com.example.meme.pageDTOs;

import com.example.meme.dto.ProductDTO;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class ProductDTOPage extends PageImpl<ProductDTO> {

    public ProductDTOPage(List<ProductDTO> content ,int page ,int size ,int total) {
        super(content ,PageRequest.of(page ,size) ,total);
    }

    public ProductDTOPage(List<ProductDTO> content) {
        super(content);
    }
}
