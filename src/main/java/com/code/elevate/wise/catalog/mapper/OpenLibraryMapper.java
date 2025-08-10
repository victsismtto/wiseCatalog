package com.code.elevate.wise.catalog.mapper;

import com.code.elevate.wise.catalog.domain.dto.SubjectDTO;
import com.code.elevate.wise.catalog.domain.entity.BookEntity;

public interface OpenLibraryMapper {
    BookEntity toBookEntity(SubjectDTO.Work work, String genre);
}
