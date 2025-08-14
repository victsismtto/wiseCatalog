package com.code.elevate.wise.catalog.app.controller;

import com.code.elevate.wise.catalog.domain.dto.BookDTO;
import com.code.elevate.wise.catalog.app.service.BooksService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class BooksControllerTest {

    @InjectMocks private BooksController controller;
    @Mock private BooksService service;

    @Test
    void getAllBooksTest() throws JsonProcessingException {
        ResponseEntity<List<BookDTO>> responseGetAll = controller.getAllBooks(0, 10);
        Assertions.assertTrue(responseGetAll.getStatusCode().is2xxSuccessful());
    }

    @Test
    void getByIdTest() throws JsonProcessingException {
        ResponseEntity<BookDTO> responseGetById =controller.getById("id");
        Assertions.assertTrue(responseGetById.getStatusCode().is2xxSuccessful());
    }

    @Test
    void getByGenreTest() {
        ResponseEntity<List<BookDTO>> getByGenre = controller.getByGenre("genre");
        Assertions.assertTrue(getByGenre.getStatusCode().is2xxSuccessful());
    }

    @Test
    void getByAuthorTest() {
        ResponseEntity<List<BookDTO>> getByAuthor = controller.getByAuthor("author");
        Assertions.assertTrue(getByAuthor.getStatusCode().is2xxSuccessful());
    }

    @Test
    void getRecentsTest() throws JsonProcessingException {
        ResponseEntity<List<BookDTO>> getByAuthor = controller.getRecents();
        Assertions.assertTrue(getByAuthor.getStatusCode().is2xxSuccessful());
    }

}
