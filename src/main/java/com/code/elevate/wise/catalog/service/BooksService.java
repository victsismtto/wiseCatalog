package com.code.elevate.wise.catalog.service;

import com.code.elevate.wise.catalog.domain.entity.BookEntity;

import java.util.List;

public interface BooksService {
    public List<BookEntity> findAllBooks();
}
