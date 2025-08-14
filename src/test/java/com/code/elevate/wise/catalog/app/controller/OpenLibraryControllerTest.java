package com.code.elevate.wise.catalog.app.controller;

import com.code.elevate.wise.catalog.app.service.OpenLibraryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OpenLibraryControllerTest {

    @InjectMocks private OpenLibraryController controller;
    @Mock private OpenLibraryService service;

    @Test
    void createAllBooksTest() {
        List<String> genre = List.of("triller", "fantasy");
        Assertions.assertDoesNotThrow(() -> controller.createAllBooks(genre));
    }
    @Test
    void deleteAllBooksTest() {
        Assertions.assertDoesNotThrow(() -> controller.deleteAllBooks());
    }
}
