package com.code.elevate.wise.catalog.mapper;

import com.code.elevate.wise.catalog.domain.dto.BookDTO;
import com.code.elevate.wise.catalog.domain.entity.BookEntity;


public interface BooksMapper {
    BookDTO toDTO(BookEntity book);
}
