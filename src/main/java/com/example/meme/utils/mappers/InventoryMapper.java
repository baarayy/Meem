package com.example.meme.utils.mappers;

import com.example.meme.dto.InventoryDTO;
import com.example.meme.models.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryMapper {
    public Inventory toEntity(InventoryDTO x){
        var i = new Inventory();
        i.setQuantity(x.quantity());
        return i;
    }

    public InventoryDTO toDTO(Inventory i){
        return new InventoryDTO(i.getId(),i.getQuantity());
    }
}
