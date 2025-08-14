package com.code.elevate.wise.catalog.domain.mapper;

import com.code.elevate.wise.catalog.domain.dto.SubjectDTO;
import com.code.elevate.wise.catalog.domain.dto.WorkDTO;
import com.code.elevate.wise.catalog.domain.entity.BookEntity;

public interface OpenLibraryMapper {
    BookEntity toBookEntity(WorkDTO work, String genre);
}
