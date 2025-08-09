package com.code.elevate.wise.catalog.service;

import com.code.elevate.wise.catalog.domain.entity.BookEntity;

import java.util.List;

public interface BooksService {
    List<BookEntity> findAllBooks();
    BookEntity findById(String id);
    List<BookEntity> findByGenre(String genre);
    List<BookEntity> findByAuthor(String author);
}
