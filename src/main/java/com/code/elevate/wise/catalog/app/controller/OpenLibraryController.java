package com.code.elevate.wise.catalog.app.controller;

import com.code.elevate.wise.catalog.app.service.OpenLibraryService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/openLibrary")
public class OpenLibraryController {

    @Autowired private OpenLibraryService service;

    @PostMapping("/create")
    public ResponseEntity<Void> createAllBooks(@RequestBody List<String> list)  {
        log.info("starting controller - createAllBooks");
        service.createListOfBooks(list);
        return ResponseEntity.created(URI.create("/openLibrary/create")).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAllBooks()  {
        log.info("starting controller - deleteAllBooks");
        service.deleteListOfBooks();
        return ResponseEntity.noContent().build();
    }
}
