package com.code.elevate.wise.catalog.domain.mapper;

import com.code.elevate.wise.catalog.domain.dto.BookDTO;
import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import com.code.elevate.wise.catalog.domain.mapper.impl.BooksMapperImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BooksMapperImplTest {

    private BooksMapperImpl mapper;

    @BeforeEach
    void setup() {
        mapper = new BooksMapperImpl();
    }

    @Test
    void toDTO_shouldReturnNull_whenBookIsNull() {
        BookDTO dto = mapper.toDTO(null);
        assertNull(dto);
    }

    @Test
    void toDTO_shouldMapAllFieldsCorrectly() {
        ObjectId id = new ObjectId();
        BookEntity book = new BookEntity();
        book.set_id(id);
        book.setTitle("Harry Potter");
        book.setAuthor("J.K. Rowling");
        book.setPublicationYear(2001);
        book.setGenre("Fantasy");

        BookDTO dto = mapper.toDTO(book);

        assertNotNull(dto);
        assertEquals(id.toHexString(), dto.getId());
        assertEquals("Harry Potter", dto.getTitle());
        assertEquals("J.K. Rowling", dto.getAuthor());
        assertEquals(2001, dto.getPublicationYear());
        assertEquals("Fantasy", dto.getGenre());
    }
}
