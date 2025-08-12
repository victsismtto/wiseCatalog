package com.code.elevate.wise.catalog.domain.mapper;

import com.code.elevate.wise.catalog.domain.dto.SubjectDTO;
import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import com.code.elevate.wise.catalog.domain.mapper.impl.OpenLibraryMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OpenLibraryMapperImplTest {

    @InjectMocks
    private OpenLibraryMapperImpl mapper;

    @Mock
    private MongoOperations mongoOperations;

    @Test
    void toBookEntity_shouldMapAllFields_whenAuthorsAndGenreExist() {
        SubjectDTO.Work work = new SubjectDTO.Work();
        work.setTitle("Test Title");
        work.setFirstPublishYear(2020);
        SubjectDTO.Author author = new SubjectDTO.Author();
        author.setName("Name");
        work.setAuthors(List.of(author));

        BookEntity book = mapper.toBookEntity(work, "Fantasy");

        assertEquals("Test Title", book.getTitle());
        assertEquals("Name", book.getAuthor());
        assertEquals(2020, book.getPublicationYear());
        assertEquals("Fantasy", book.getGenre());
    }

    @Test
    void toBookEntity_shouldSetAuthorUnknown_whenNoAuthors() {
        SubjectDTO.Work work = new SubjectDTO.Work();
        work.setTitle("Test Title");
        work.setFirstPublishYear(2020);
        work.setAuthors(Collections.emptyList());

        BookEntity book = mapper.toBookEntity(work, "SciFi");

        assertEquals("Unknown", book.getAuthor());
    }

    @Test
    void toBookEntity_shouldSetGenreUnknown_whenGenreIsNull() {
        SubjectDTO.Work work = new SubjectDTO.Work();
        work.setTitle("Test Title");
        work.setFirstPublishYear(2020);
        SubjectDTO.Author author = new SubjectDTO.Author();
        author.setName("Name");
        work.setAuthors(List.of(author));

        BookEntity book = mapper.toBookEntity(work, null);

        assertEquals("Unknown", book.getGenre());
    }
}

