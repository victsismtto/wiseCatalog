package com.code.elevate.wise.catalog.domain.mapper.impl;

import com.code.elevate.wise.catalog.domain.dto.SubjectDTO;
import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import com.code.elevate.wise.catalog.domain.mapper.OpenLibraryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
public class OpenLibraryMapperImpl implements OpenLibraryMapper {

    @Autowired private MongoOperations mongoOperations;

    @Override
    public BookEntity toBookEntity(SubjectDTO.Work work, String genre) {
        BookEntity book = new BookEntity();
        book.setTitle(work.getTitle());
        if (work.getAuthors() != null && !work.getAuthors().isEmpty()) {
            book.setAuthor(work.getAuthors().get(0).getName());
        } else {
            book.setAuthor("Unknown");
        }
        book.setPublicationYear(work.getFirstPublishYear());
        book.setGenre(genre != null ? genre : "Unknown");

        return book;
    }
}
