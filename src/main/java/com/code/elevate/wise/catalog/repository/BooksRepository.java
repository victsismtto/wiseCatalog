package com.code.elevate.wise.catalog.repository;

import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends MongoRepository<BookEntity, String> {
    Optional<List<BookEntity>> findByGenre(String genre);
    Optional<List<BookEntity>> findByAuthor(String author);
    Optional<BookEntity> findByTitle(String title);
}
