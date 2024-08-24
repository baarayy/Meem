package com.example.meme.pageDTOs;

import com.example.meme.dto.UserPaymentDTO;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class UserPaymentDTOPage extends PageImpl<UserPaymentDTO> {

    public UserPaymentDTOPage(List<UserPaymentDTO> content, int page, int size, long total) {
        super(content, PageRequest.of(page, size), total);
    }

    public UserPaymentDTOPage(List<UserPaymentDTO> content) {
        super(content);
    }
}
