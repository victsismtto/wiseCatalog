package com.code.elevate.wise.catalog.app.controller;

import com.code.elevate.wise.catalog.app.service.OpenLibraryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OpenLibraryControllerTest {

    @InjectMocks private OpenLibraryController controller;
    @Mock private OpenLibraryService service;

    @Test
    void createAllBooksTest() {
        controller.createAllBooks();
        Assertions.assertDoesNotThrow(() -> controller.createAllBooks());
    }
    @Test
    void deleteAllBooksTest() {
        controller.deleteAllBooks();
        Assertions.assertDoesNotThrow(() -> controller.deleteAllBooks());
    }
}
