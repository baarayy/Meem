package com.example.meme;

import com.example.meme.dto.CategoryDTO;
import com.example.meme.exception.EntityNotFoundException;
import com.example.meme.models.Category;
import com.example.meme.repositories.CategoryRepo;
import com.example.meme.repositories.ProductRepo;
import com.example.meme.service.CategoryService;
import com.example.meme.utils.mappers.CategoryMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.*;
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
public class CategoryServiceTest {
    @Mock
    private CategoryRepo repo;
    @Mock
    private ProductRepo productRepo;
    @Mock
    private CategoryMapper mapper;
    @Mock
    private Validator validator;

    @InjectMocks
    private CategoryService service;

    private Category c = new Category(1,"name","desc",new ArrayList<>());
    private CategoryDTO x = new CategoryDTO(1,"name","desc",null);

    public CategoryServiceTest() {
    }

    @BeforeAll
    public static void setUpClass() {

    }

    @AfterAll
    public static void tearDownClass() {

    }

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        service.setValidator(validator);
    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    void testCreate(){
        when(mapper.toEntity(x)).thenReturn(c);
        when(repo.save(c)).thenReturn(c);
        when(mapper.toDTO(c)).thenReturn(x);

        assertEquals(x, service.create(x));

        verify(repo,times(1)).save(c);
    }

    @Test
    void testFindById(){
        when(repo.findById(1)).thenReturn(Optional.of(c));
        when(mapper.toDTO(c)).thenReturn(x);

        assertEquals(x,service.findById(1));
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
        Page<Category> productPage = new PageImpl<>(List.of(c));

        when(repo.findAll(pageable)).thenReturn(productPage);
        when(mapper.toDTO(c)).thenReturn(x);
        assertEquals(x, service.findAll(0, 10).getContent().get(0));
    }

    @Test
    void testFindAllWithNegativeArguments(){
        int page = -1;
        int size = 10;
        assertThrows(IllegalArgumentException.class, () -> service.findAll(page, size));
    }

    @Test
    void testUpdate(){
        when(repo.findById(1)).thenReturn(Optional.of(c));
        when(repo.save(c)).thenReturn(c);
        when(mapper.toDTO(c)).thenReturn(x);

        assertEquals(x,service.update(1, x));

        verify(repo,times(1)).save(c);
    }

    @Test
    void testDelete(){
        doNothing().when(repo).delete(c);
        when(repo.findById(1)).thenReturn(Optional.of(c));
        service.delete(1);
        verify(repo,times(1)).delete(c);
    }


}
