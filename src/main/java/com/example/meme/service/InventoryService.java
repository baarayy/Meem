package com.example.meme.service;

import com.example.meme.dto.InventoryDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.repositories.InventoryRepo;
import com.example.meme.utils.mappers.InventoryMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepo repo;
    private final InventoryMapper mapper;
    private Validator validator;

    @Transactional
    @CacheEvict(value={"allInventories", "inventoryById"}, allEntries=true)
    public InventoryDTO create(InventoryDTO x) {
        var violations = validator.validate(x);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var inventory = mapper.toEntity(x);
        var saved = repo.save(inventory);
        return mapper.toDTO(saved);
    }

    @Cacheable(value="allInventories", key = "'findAll_' + #page + '_' + #size")
    public Page<InventoryDTO> findALl(int page ,int size) {
        return repo.findAll(PageRequest.of(page , size)).map(mapper::toDTO);
    }

    @Cacheable(value="orderItemById", key="#id")
    public InventoryDTO findById(Integer id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        return repo.findById(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("There is no inventory with id " + id));
    }

    @Transactional
    @CacheEvict(value={"allInventories", "inventoryById"}, allEntries=true)
    public InventoryDTO update(Integer id ,InventoryDTO x) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        var violations = validator.validate(x);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var inventory = repo.findById(id).orElseThrow(()->
                new EntityNotFoundException("There is no inventory with id " + id));
        inventory.setQuantity(x.quantity());
        var saved = repo.save(inventory);
        return mapper.toDTO(saved);
    }

    @Transactional
    @CacheEvict(value={"allInventories", "inventoryById"}, allEntries=true)
    public void delete(Integer id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        repo.findById(id).ifPresent(repo::delete);
    }

    @CacheEvict(value={"allInventories", "inventoryById"}, allEntries=true)
    public void clearCache() {}
}
