package com.example.meme.utils.mappers;

import com.example.meme.dto.DiscountDTO;
import com.example.meme.models.Discount;
import com.example.meme.repositories.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountMapper {
    private final ProductRepo productRepo;

    public Discount toEntity(DiscountDTO x){
        var d = new Discount();
        d.setActive(x.active());
        d.setDesc(x.desc());
        d.setName(x.name());
        d.setPercent(x.percent());
        var list = x.productIds();
        if (list != null) {
            d.setProducts(productRepo.findAllById(list));
        }
        return d;
    }

    public DiscountDTO toDTO(Discount d){
        var list = d.getProducts().stream().map(x -> x.getId()).collect(Collectors.toList());
        return new DiscountDTO(d.getId(),d.getName(),d.getDesc(),d.getPercent(),d.getActive(),list);
    }
}
