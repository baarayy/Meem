package com.example.meme.service;

import com.example.meme.dto.InventoryDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.repositories.InventoryRepo;
import com.example.meme.utils.mappers.InventoryMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepo repo;
    private final InventoryMapper mapper;

    @Transactional
    public InventoryDTO create(InventoryDTO x) {
        var inventory = mapper.toEntity(x);
        var saved = repo.save(inventory);
        return mapper.toDTO(saved);
    }

    public Page<InventoryDTO> findALl(int page ,int size) {
        return repo.findAll(PageRequest.of(page , size)).map(mapper::toDTO);
    }

    public InventoryDTO findById(Integer id) {
        return repo.findById(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("There is no inventory with id " + id));
    }

    @Transactional
    public InventoryDTO update(Integer id ,InventoryDTO x) {
        var inventory = repo.findById(id).orElseThrow(()->
                new EntityNotFoundException("There is no inventory with id " + id));
        inventory.setQuantity(x.quantity());
        var saved = repo.save(inventory);
        return mapper.toDTO(saved);
    }

    @Transactional
    public void delete(Integer id) {
        repo.findById(id).ifPresent(repo::delete);
    }
}
