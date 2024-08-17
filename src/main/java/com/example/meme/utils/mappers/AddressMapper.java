package com.example.meme.utils.mappers;

import com.example.meme.dto.UserAddressDTO;
import com.example.meme.models.User;
import com.example.meme.models.UserAddress;
import com.example.meme.repositories.AddressRepo;
import com.example.meme.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressMapper {
    private final AddressRepo repo;
    private final UserRepo userRepo;

    public UserAddress toEntity(UserAddressDTO x) {
        var a = new UserAddress();
        a.setAddressLine1(x.addressLine1());
        a.setAddressLine2(x.addressLine2());
        a.setCity(x.city());
        a.setPostalCode(x.postalCode());
        a.setCountry(x.country());
        var user = userRepo.findById(x.userId());
        if(user.isPresent())
            a.setUser(user.get());
        return  a;
    }

    public UserAddressDTO toDTO(UserAddress a){
        return a.getUser()!=null ? new UserAddressDTO(a.getId(),a.getUser().getId(),
                a.getAddressLine1(),a.getAddressLine2(),a.getCity(),a.getPostalCode(),a.getCountry()) : null;
    }
}
