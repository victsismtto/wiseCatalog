package com.code.elevate.wise.catalog.controller;

import com.code.elevate.wise.catalog.service.OpenLibraryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/openLibrary")
public class OpenLibraryController {

    @Autowired
    private OpenLibraryService service;

    @PostMapping("/create")
    public ResponseEntity<?> createAllBooks()  {
        log.info("starting controller - createAllBooks");
        service.createListOfBooks();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAllBooks()  {
        log.info("starting controller - deleteAllBooks");
        service.deleteListOfBooks();
        return ResponseEntity.ok().build();
    }
}
