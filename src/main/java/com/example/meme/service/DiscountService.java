package com.example.meme.service;

import com.example.meme.dto.DiscountDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.models.Discount;
import com.example.meme.repositories.DiscountRepo;
import com.example.meme.repositories.ProductRepo;
import com.example.meme.utils.mappers.DiscountMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final DiscountRepo repo;
    private final ProductRepo productRepo;
    private final DiscountMapper mapper;
    private Validator validator;

    public Page<DiscountDTO> findAll(int page ,int size) {
        return  repo.findAll(PageRequest.of(page , size)).map(mapper::toDTO);
    }

    public DiscountDTO findById(Integer id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        return repo.findById(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("There is no discount with id " + id));
    }

    @Transactional
    public DiscountDTO create(DiscountDTO x) {
        var violations = validator.validate(x);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var  d = mapper.toEntity(x);
        var saved = repo.save(d);
        return mapper.toDTO(saved);
    }

    @Transactional
    public DiscountDTO update(Integer id,DiscountDTO x) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        var violations = validator.validate(x);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var d = repo.findById(id).orElseThrow(()->
                new EntityNotFoundException("There is no discount with id " + id));
        d.setActive(x.active());
        d.setDesc(x.desc());
        d.setName(x.name());
        d.setPercent(x.percent());
        var list = x.productIds();
        if(list != null) {
            d.setProducts(productRepo.findAllById(list));
        }
        var saved = repo.save(d);
        return mapper.toDTO(saved);
    }

    @Transactional
    public void delete(Integer id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        var d = repo.findById(id);
        if(d.isPresent()) {
            var dd = d.get();
            var list = dd.getProducts();
            list.forEach(dd::removeProduct);
            productRepo.saveAll(list);
            repo.delete(dd);
        }
    }
}
