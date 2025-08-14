package com.code.elevate.wise.catalog.domain.mapper.impl;

import com.code.elevate.wise.catalog.domain.dto.AuthorDTO;
import com.code.elevate.wise.catalog.domain.dto.SubjectDTO;
import com.code.elevate.wise.catalog.domain.dto.WorkDTO;
import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import com.code.elevate.wise.catalog.domain.mapper.OpenLibraryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OpenLibraryMapperImpl implements OpenLibraryMapper {

    @Autowired private MongoOperations mongoOperations;

    @Override
    public BookEntity toBookEntity(WorkDTO work, String genre) {
        BookEntity book = new BookEntity();
        book.setTitle(work.getTitle());
        String author = Optional.ofNullable(work.getAuthors())
                .flatMap(authors -> authors.stream().findFirst())
                .map(AuthorDTO::getName)
                .orElse("Unknown");
        book.setAuthor(author);
        book.setPublicationYear(work.getFirstPublishYear());
        book.setGenre(genre != null ? genre : "Unknown");

        return book;
    }
}
