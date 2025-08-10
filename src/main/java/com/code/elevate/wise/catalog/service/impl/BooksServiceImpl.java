package com.code.elevate.wise.catalog.service.impl;

import com.code.elevate.wise.catalog.domain.dto.BookDTO;
import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import com.code.elevate.wise.catalog.exception.NotFoundException;
import com.code.elevate.wise.catalog.mapper.BooksMapper;
import com.code.elevate.wise.catalog.repository.BooksRepository;
import com.code.elevate.wise.catalog.service.BooksService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.management.RuntimeErrorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
public class BooksServiceImpl implements BooksService {

    @Autowired private BooksMapper mapper;
    @Autowired private BooksRepository repository;
    @Autowired private RedisTemplate<String, Object> redis;
    @Autowired private ObjectMapper objectMapper;

    @Override
    public List<BookDTO> findAllBooks(int page, int pageSize) throws JsonProcessingException {
        if (redis.hasKey("list")) {
            String redisObject = (String) redis.opsForValue().get("list");
            JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, BookDTO.class);
            return objectMapper.readValue(redisObject, type);
        } else {
            List<BookEntity> listOfBooks = repository.findAll(PageRequest.of(page, pageSize)).getContent();
            if (listOfBooks.isEmpty()) {
                throw new NotFoundException("no book was found");
            }
            List<BookDTO> dtoResponse = new ArrayList<>(listOfBooks.size());
            listOfBooks.forEach(entity -> dtoResponse.add(mapper.toDTO(entity)));
            String value = objectMapper.writeValueAsString(dtoResponse);
            redis.opsForValue().set("list", value);
            redis.expire("list", 5, TimeUnit.MINUTES);
            return dtoResponse;
        }
    }

    @Override
    public BookDTO findById(String id) throws JsonProcessingException {
        if (redis.hasKey(id)) {
            String redisObject = (String) redis.opsForValue().get(id);
            JavaType type = objectMapper.getTypeFactory().constructType(BookDTO.class);
            return objectMapper.readValue(redisObject, type);
        } else {
            Optional<BookEntity> optionalBook = repository.findById(id);
            if (optionalBook.isEmpty()) {
                throw new NotFoundException("book id " + id + " was not found");
            }
            BookDTO bookDTO = mapper.toDTO(optionalBook.get());
            String value = objectMapper.writeValueAsString(bookDTO);
            redis.opsForValue().set(id, value);
            redis.expire(id, 5, TimeUnit.MINUTES);
            return bookDTO;
        }
    }

    @Override
    public List<BookDTO> findByGenre(String genre) {
        Optional<List<BookEntity>> optionalBook = repository.findByGenre(genre);
        if (optionalBook.isEmpty() || optionalBook.map(List::isEmpty).orElse(false)) {
            throw new NotFoundException("no book with the " + genre + " genre was found");
        }
        List<BookDTO> dtoResponse = new ArrayList<>(optionalBook.get().size());
        optionalBook.get().forEach(entity -> dtoResponse.add(mapper.toDTO(entity)));
        return dtoResponse;
    }

    @Override
    public List<BookDTO> findByAuthor(String author) {
        Optional<List<BookEntity>> optionalBook = repository.findByAuthor(author);
        if (optionalBook.isEmpty() || optionalBook.map(List::isEmpty).orElse(false)) {
            throw new NotFoundException("no book with the author named " + author + " was found");
        }
        List<BookDTO> dtoResponse = new ArrayList<>(optionalBook.get().size());
        optionalBook.get().forEach(entity -> dtoResponse.add(mapper.toDTO(entity)));
        return dtoResponse;
    }
}
