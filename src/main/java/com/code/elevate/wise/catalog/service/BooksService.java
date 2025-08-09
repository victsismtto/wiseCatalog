package com.code.elevate.wise.catalog.service;

import com.code.elevate.wise.catalog.domain.dto.BookDTO;
import com.code.elevate.wise.catalog.domain.entity.BookEntity;

import java.util.List;

public interface BooksService {
    List<BookEntity> findAllBooks();
    BookDTO findById(String id);
    List<BookDTO> findByGenre(String genre);
    List<BookDTO> findByAuthor(String author);
}
