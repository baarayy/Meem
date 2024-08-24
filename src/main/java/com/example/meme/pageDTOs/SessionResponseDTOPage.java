package com.example.meme.pageDTOs;

import com.example.meme.dto.SessionResponseDTO;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class SessionResponseDTOPage extends PageImpl<SessionResponseDTO> {

    public SessionResponseDTOPage(List<SessionResponseDTO> content, int page, int size, long total) {
        super(content, PageRequest.of(page, size), total);
    }

    public SessionResponseDTOPage(List<SessionResponseDTO> content) {
        super(content);
    }

}