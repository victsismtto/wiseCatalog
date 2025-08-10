package com.code.elevate.wise.catalog.service;

import com.code.elevate.wise.catalog.domain.dto.BookDTO;
import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface BooksService {
    List<BookDTO> findAllBooks(int page, int pageSize) throws JsonProcessingException;
    BookDTO findById(String id) throws JsonProcessingException;
    List<BookDTO> findByGenre(String genre);
    List<BookDTO> findByAuthor(String author);
}
