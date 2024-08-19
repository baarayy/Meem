package com.example.meme.service;

import com.example.meme.dto.UserAddressDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.models.UserAddress;
import com.example.meme.repositories.AddressRepo;
import com.example.meme.repositories.UserRepo;
import com.example.meme.utils.mappers.AddressMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAddressService {
    private final AddressRepo repo;
    private final UserRepo userRepo;
    private final AddressMapper mapper;
    private Validator validator;

    @Transactional
    public UserAddressDTO create(UserAddressDTO x) {
        var violations = validator.validate(x);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var address = mapper.toEntity(x);
        var saved = repo.save(address);
        return mapper.toDTO(saved);
    }

    public UserAddressDTO findById(Integer id) {
        return repo.findById(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("There is no address with id " + id));
    }

    public Page<UserAddressDTO> findAll(int page ,int size) {
        return repo.findAll(PageRequest.of(page , size)).map(mapper::toDTO);
    }

    @Transactional
    public UserAddressDTO update(Integer id ,UserAddressDTO x) {
        var violations = validator.validate(x);
        if(!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        var address = repo.findById(id).orElseThrow(()->
                new EntityNotFoundException("There is no address with id " + id));
        address.setAddressLine1(x.addressLine1());
        address.setAddressLine2(x.addressLine2());
        address.setCity(x.city());
        address.setCountry(x.country());
        address.setPostalCode(x.postalCode());
        userRepo.findById(x.userId()).ifPresent(address::setUser);
        var saved = repo.save(address);
        return mapper.toDTO(saved);
    }

    @Transactional
    public void delete(Integer id) {
        repo.findById(id).ifPresent(repo::delete);
    }

    public UserAddressDTO findAddressByUserId(Integer id) {
        return repo.findByUserId(id).map(mapper::toDTO).orElseThrow(()->
                new EntityNotFoundException("There is no address with user id " + id));
    }
}
