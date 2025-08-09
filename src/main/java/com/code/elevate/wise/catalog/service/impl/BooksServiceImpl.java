package com.code.elevate.wise.catalog.service.impl;

import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import com.code.elevate.wise.catalog.repository.BooksRepository;
import com.code.elevate.wise.catalog.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksServiceImpl implements BooksService {

    @Autowired private BooksRepository repository;

    @Override
    public List<BookEntity> findAllBooks() {
        return repository.findAll();
    }
}
