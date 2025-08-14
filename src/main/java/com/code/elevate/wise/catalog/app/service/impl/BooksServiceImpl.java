package com.code.elevate.wise.catalog.app.service.impl;

import com.code.elevate.wise.catalog.domain.dto.BookDTO;
import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import com.code.elevate.wise.catalog.domain.exception.NotFoundException;
import com.code.elevate.wise.catalog.domain.mapper.BooksMapper;
import com.code.elevate.wise.catalog.app.repository.BooksRepository;
import com.code.elevate.wise.catalog.app.service.BooksService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
        log.info("getting the book - database");
        List<BookEntity> listOfBooks = repository.findAll(PageRequest.of(page, pageSize)).getContent();
        if (listOfBooks.isEmpty()) {
            throw new NotFoundException("no book was found");
        }
        List<BookDTO> dtoResponse = new ArrayList<>(listOfBooks.size());
        listOfBooks.forEach(entity -> dtoResponse.add(mapper.toDTO(entity)));
        return dtoResponse;
    }

    @Override
    public BookDTO findById(String id) throws JsonProcessingException {
        if (redis.hasKey(id)) {
            String redisObject = (String) redis.opsForValue().get(id);
            JavaType type = objectMapper.getTypeFactory().constructType(BookDTO.class);
            return objectMapper.readValue(redisObject, type);
        }
        BookEntity optionalBook = repository.findById(id).orElseThrow(
                () -> new NotFoundException("book id " + id + " was not found"));
        BookDTO bookDTO = mapper.toDTO(optionalBook);
        if (redis.hasKey("recents")) {
            updateRecentsRedis(bookDTO);
        } else {
            saveRecentsRedis(bookDTO);
        }
        saveCacheById(bookDTO);
        return bookDTO;
    }

    @Override
    public List<BookDTO> findByGenre(String genre) {
        Optional<List<BookEntity>> optionalBook = repository.findByGenre(genre);
        if (optionalBook.isEmpty() || optionalBook.map(List::isEmpty).orElse(false)) {
            throw new NotFoundException("no book with the " + genre + " genre was found");
        }
        log.info("found in the database [findByGenre]");
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
        log.info("found in the database [findByAuthor]");
        List<BookDTO> dtoResponse = new ArrayList<>(optionalBook.get().size());
        optionalBook.get().forEach(entity -> dtoResponse.add(mapper.toDTO(entity)));
        return dtoResponse;
    }

    @Override
    public List<BookDTO> findMostRecents() throws JsonProcessingException {
        log.info("getting the book - redis [findMostRecents]");
        if (redis.hasKey("recents")) {
            String redisObject = (String) redis.opsForValue().get("recents");
            JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, BookDTO.class);
            return objectMapper.readValue(redisObject, type);
        }
        log.info("there is no new fetch yet");
        return List.of();
    }

    private void saveRecentsRedis(BookDTO bookDTO) throws JsonProcessingException {
        log.info("creating recents - redis [findById]");
        List<BookDTO> initialList = List.of(bookDTO);
        String initialRecents = objectMapper.writeValueAsString(initialList);
        redis.opsForValue().set("recents", initialRecents);
        redis.expire("recents", 60, TimeUnit.MINUTES);
    }

    private void updateRecentsRedis(BookDTO bookDTO) throws JsonProcessingException {
        log.info("adding in recents - redis [findById]");
        String redisObject = (String) redis.opsForValue().get("recents");
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, BookDTO.class);
        List<BookDTO> recentsList = objectMapper.readValue(redisObject, type);
        Optional<BookDTO> duplicate = recentsList.stream().filter(book -> book.getId().equals(bookDTO.getId())).findFirst();
        if (duplicate.isEmpty()) {
            recentsList.add(bookDTO);
            String initialRecents = objectMapper.writeValueAsString(recentsList);
            redis.opsForValue().set("recents", initialRecents);
            redis.expire("recents", 60, TimeUnit.MINUTES);
        }
    }

    private void saveCacheById(BookDTO bookDTO) throws JsonProcessingException {
        redis.opsForValue().set(bookDTO.getId(), objectMapper.writeValueAsString(bookDTO));
        redis.expire(bookDTO.getId(), 5, TimeUnit.MINUTES);
    }
}
