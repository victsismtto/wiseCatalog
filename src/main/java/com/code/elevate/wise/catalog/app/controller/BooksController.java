package com.code.elevate.wise.catalog.app.controller;


import com.code.elevate.wise.catalog.domain.dto.BookDTO;
import com.code.elevate.wise.catalog.app.service.BooksService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Log4j2
@RestController
@RequestMapping("/books")
public class BooksController {

    @Autowired private BooksService service;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        log.info("starting controller - getAllBooks");
        return ResponseEntity.ok(service.findAllBooks(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getById(@PathVariable("id") String id) throws JsonProcessingException {
        log.info("starting controller - getById");
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<BookDTO>> getByGenre(@PathVariable("genre") String genre) {
        log.info("starting controller - getByGenre");
        return ResponseEntity.ok(service.findByGenre(genre));
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<BookDTO>> getByAuthor(@PathVariable("author") String author) {
        log.info("starting controller - getByAuthor");
        return ResponseEntity.ok(service.findByAuthor(author));
    }

    @GetMapping("/recents")
    public ResponseEntity<List<BookDTO>> getRecents() throws JsonProcessingException {
        log.info("starting controller - getRecents");
        return ResponseEntity.ok(service.findMostRecents());
    }
}
