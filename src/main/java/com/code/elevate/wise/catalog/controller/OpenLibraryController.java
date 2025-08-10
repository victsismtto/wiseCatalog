package com.code.elevate.wise.catalog.controller;

import com.code.elevate.wise.catalog.domain.dto.BookDTO;
import com.code.elevate.wise.catalog.service.OpenLibraryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/openLibrary")
public class OpenLibraryController {

    @Autowired
    private OpenLibraryService service;

    @PostMapping("/create")
    public ResponseEntity<?> getAllBooks()  {
        service.createListOfBooks();
        return ResponseEntity.ok().build();
    }
}
