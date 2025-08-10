package com.code.elevate.wise.catalog.mapper.impl;

import com.code.elevate.wise.catalog.domain.dto.BookDTO;
import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import com.code.elevate.wise.catalog.mapper.BooksMapper;
import org.springframework.stereotype.Component;

@Component
public class BooksMapperImpl implements BooksMapper {
    @Override
    public BookDTO toDTO(BookEntity book) {
        if (book == null) return null;
        return new BookDTO(
                book.get_id().toHexString(), book.getTitle(), book.getAuthor(),
                book.getPublicationYear(), book.getGenre()
        );
    }
}
