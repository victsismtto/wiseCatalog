package com.code.elevate.wise.catalog.app.service;


import com.code.elevate.wise.catalog.app.repository.BooksRepository;
import com.code.elevate.wise.catalog.app.service.impl.BooksServiceImpl;
import com.code.elevate.wise.catalog.domain.dto.BookDTO;
import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import com.code.elevate.wise.catalog.domain.exception.NotFoundException;
import com.code.elevate.wise.catalog.domain.mapper.BooksMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BooksServiceImplTest {

    @InjectMocks private BooksServiceImpl service;
    @Mock private BooksRepository repository;
    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private BooksMapper mapper;
    @Mock private ValueOperations<String, Object> valueOperations;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(service, "objectMapper", objectMapper);
    }

    @Test
    void findAllBooksTest() throws JsonProcessingException {
        BookEntity entity = buildEntity();
        Page<BookEntity> page = new PageImpl<>(List.of(entity));
        BookDTO bookDTO = buildDTO();
        when(mapper.toDTO(entity)).thenReturn(bookDTO);
        when(repository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        List<BookDTO> bookDTOS = service.findAllBooks(0, 10);
        assertEquals(bookDTOS, List.of(bookDTO));
    }

    @Test
    void findAllBooksErrorTest() {
        Page<BookEntity> page = new PageImpl<>(List.of());
        when(repository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        Assertions.assertThrows(NotFoundException.class, () -> service.findAllBooks(0, 10));
    }

    @Test
    void findByIdRedisTest() throws JsonProcessingException {
        BookDTO dto = buildDTO();
        when(redisTemplate.hasKey(any())).thenReturn(true);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(jsonStringDTO());
        BookDTO bookDTO = service.findById("1");
        assertEquals(bookDTO, dto);
    }


    @Test
    void findByIdNoRedisTest() throws JsonProcessingException {
        BookEntity entity = buildEntity();
        BookDTO dto = buildDTO();
        when(repository.findById("1")).thenReturn(Optional.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);
        when(redisTemplate.hasKey(any())).thenReturn(false);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        BookDTO bookDTO = service.findById("1");
        assertEquals(bookDTO, dto);
    }

    @Test
    void findByIdErrorTest() {
        when(repository.findById("1")).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> service.findById("1"));
    }

    @Test
    void findByIdTest() throws JsonProcessingException {
        BookEntity entity = buildEntity();
        BookDTO dto = buildDTO();
        when(repository.findById("1")).thenReturn(Optional.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);
        when(redisTemplate.hasKey("1")).thenReturn(false);
        when(redisTemplate.hasKey("recents")).thenReturn(true);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        String jsonFromRedis = jsonStringList();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(jsonFromRedis);
        BookDTO response = service.findById("1");
        Assertions.assertEquals(response.getAuthor(), dto.getAuthor());
    }

    @Test
    void findByGenre() {
        BookEntity entity = buildEntity();
        BookDTO dto = buildDTO();
        when(repository.findByGenre(any())).thenReturn(Optional.of(List.of(entity)));
        when(mapper.toDTO(entity)).thenReturn(dto);
        service.findByGenre("fantasy");
    }

    @Test
    void findByGenreError() {
        when(repository.findByGenre(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> service.findByGenre("fantasy"));
    }

    @Test
    void findByAuthor() {
        BookEntity entity = buildEntity();
        BookDTO dto = buildDTO();
        when(repository.findByAuthor("JKR")).thenReturn(Optional.of(List.of(entity)));
        when(mapper.toDTO(entity)).thenReturn(dto);
        List<BookDTO> bookDTOS = service.findByAuthor("JKR");
        assertEquals(bookDTOS, List.of(dto));
    }

    @Test
    void findByAuthorError() {
        when(repository.findByAuthor("JKR")).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> service.findByAuthor("JKR"));
    }

    @Test
    void findMostRecents() throws Exception {
        when(redisTemplate.hasKey(any())).thenReturn(true);
        String jsonFromRedis = jsonStringList();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(jsonFromRedis);
        List<BookDTO> list = service.findMostRecents();
        Assertions.assertEquals("JKR", list.get(0).getAuthor());
    }

    @Test
    void findMostRecentsEmpty() throws Exception {
        when(redisTemplate.hasKey(any())).thenReturn(false);
        List<BookDTO> list = service.findMostRecents();
        Assertions.assertEquals(list, List.of());
    }

    private BookEntity buildEntity() {
        BookEntity entity = new BookEntity();
        entity.set_id(ObjectId.get());
        entity.setAuthor("JKR");
        entity.setGenre("fantasy");
        entity.setTitle("Harry Potter");
        return entity;
    }

    private BookDTO buildDTO() {
        BookDTO dto = new BookDTO();
        dto.setId("1");
        dto.setAuthor("JKR");
        dto.setGenre("Fantasy");
        dto.setTitle("Harry Potter");
        return dto;
    }

    private String jsonStringList() {
        return "[ {\n" +
                "  \"id\" : \"1\",\n" +
                "  \"author\" : \"JKR\",\n" +
                "  \"genre\" : \"Fantasy\",\n" +
                "  \"author\" : \"JKR\",\n" +
                "  \"title\": \"Harry Potter\"\n" +
                "} ]";
    }

    private String jsonStringDTO() {
        return "{\n" +
                "  \"id\" : \"1\",\n" +
                "  \"author\" : \"JKR\",\n" +
                "  \"genre\" : \"Fantasy\",\n" +
                "  \"author\" : \"JKR\",\n" +
                "  \"title\": \"Harry Potter\"\n" +
                "}";
    }
}
