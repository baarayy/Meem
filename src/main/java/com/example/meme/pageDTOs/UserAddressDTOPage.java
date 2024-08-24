package com.example.meme.pageDTOs;

import com.example.meme.dto.UserAddressDTO;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class UserAddressDTOPage extends PageImpl<UserAddressDTO> {

    public UserAddressDTOPage(List<UserAddressDTO> content ,int page ,int size ,long total) {
        super(content, PageRequest.of(page , size) , total);
    }

    public UserAddressDTOPage(List<UserAddressDTO> content) {
        super(content);
    }
}
