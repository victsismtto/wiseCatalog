package com.code.elevate.wise.catalog.repository;

import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends MongoRepository<BookEntity, String> {
}
