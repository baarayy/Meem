package com.example.meme;

import com.example.meme.dto.OrderDTO;
import com.example.meme.dto.OrderResponseDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.models.Order;
import com.example.meme.models.PaymentDetail;
import com.example.meme.repositories.OrderItemRepo;
import com.example.meme.repositories.OrderRepo;
import com.example.meme.repositories.PaymentDetailRepo;
import com.example.meme.repositories.UserRepo;
import com.example.meme.service.OrderService;
import com.example.meme.utils.mappers.OrderMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepo repo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private OrderItemRepo orderItemRepo;
    @Mock
    private PaymentDetailRepo paymentDetailRepo;
    @Mock
    private OrderMapper mapper;

    @InjectMocks
    private OrderService service;

    private Validator validator;
    private Order o = new Order(1 , null , 0.0 , null, new ArrayList<>());
    private OrderDTO x = new OrderDTO(1 , 1 , 1 , null);
    private OrderResponseDTO responseDTO = new OrderResponseDTO(1 , 1 , 0.0 , 1 , null);

    public OrderServiceTest() {

    }

    @BeforeEach
    public void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        service.setValidator(validator);
    }

    @Test
    void testCreate(){
        when(mapper.toEntity(x)).thenReturn(o);
        when(repo.save(o)).thenReturn(o);
        when(mapper.toDTO(o)).thenReturn(responseDTO);

        assertEquals(responseDTO, service.create(x));

        verify(repo,times(1)).save(o);
    }

    @Test
    void testUpdate(){
        when(repo.findById(1)).thenReturn(Optional.of(o));
        when(repo.save(o)).thenReturn(o);
        when(mapper.toDTO(o)).thenReturn(responseDTO);

        assertEquals(responseDTO,service.update(1, x));

        verify(repo,times(1)).save(o);
    }

    @Test
    void testFindById(){
        when(repo.findById(1)).thenReturn(Optional.of(o));
        when(mapper.toDTO(o)).thenReturn(responseDTO);

        assertEquals(responseDTO,service.findById(1));
    }

    @Test
    void testFindByNegativeId(){
        assertThrows(IllegalArgumentException.class,
                () -> service.findById(-2));
    }

    @Test
    void testFindByIdNotFound(){
        when(repo.findById(2)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.findById(2));
    }

    @Test
    void testFindAll(){
        var pageable = PageRequest.of(0, 10);
        Page<Order> productPage = new PageImpl<>(List.of(o));

        when(repo.findAll(pageable)).thenReturn(productPage);
        when(mapper.toDTO(o)).thenReturn(responseDTO);
        assertEquals(responseDTO, service.findAll(0, 10).getContent().get(0));
    }

    @Test
    void testFindAllWithNegativeArguments(){
        int page = -1;
        int size = 10;
        assertThrows(IllegalArgumentException.class, () -> service.findAll(page, size));
    }

    @Test
    void testDelete(){
        doNothing().when(repo).delete(o);
        when(repo.findById(1)).thenReturn(Optional.of(o));
        service.delete(1);
        verify(repo,times(1)).delete(o);
    }

    @Test
    void testFindOrdersByUser(){
        when(repo.findByUserId(1)).thenReturn(List.of(o));
        when(mapper.toDTO(o)).thenReturn(responseDTO);
        assertEquals(responseDTO,service.findOrdersByUser(1).get(0));
    }
}
