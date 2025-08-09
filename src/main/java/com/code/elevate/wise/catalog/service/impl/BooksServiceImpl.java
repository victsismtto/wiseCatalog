package com.code.elevate.wise.catalog.service.impl;

import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import com.code.elevate.wise.catalog.exception.NotFoundException;
import com.code.elevate.wise.catalog.repository.BooksRepository;
import com.code.elevate.wise.catalog.service.BooksService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.RuntimeErrorException;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class BooksServiceImpl implements BooksService {

    @Autowired private BooksRepository repository;

    @Override
    public List<BookEntity> findAllBooks() {
        List<BookEntity> allBooks = repository.findAll();
        if (allBooks.isEmpty()) {
            throw new NotFoundException("no book was found");
        }
        return repository.findAll();
    }

    @Override
    public BookEntity findById(String id) {
        Optional<BookEntity> optionalBook = repository.findById(id);
        if (optionalBook.isEmpty()) {
            throw new NotFoundException("book id " + id + " was not found");
        }
        return optionalBook.get();
    }

    @Override
    public List<BookEntity> findByGenre(String genre) {
        Optional<List<BookEntity>> optionalBook = repository.findByGenre(genre);
        if (optionalBook.isEmpty() || optionalBook.map(List::isEmpty).orElse(false)) {
            throw new NotFoundException("no book with the " + genre + " genre was found");
        }
        return optionalBook.get();
    }

    @Override
    public List<BookEntity> findByAuthor(String author) {
        Optional<List<BookEntity>> optionalBook = repository.findByAuthor(author);
        if (optionalBook.isEmpty() || optionalBook.map(List::isEmpty).orElse(false)) {
            throw new NotFoundException("no book with the author named " + author + " was found");
        }
        return optionalBook.get();
    }
}
