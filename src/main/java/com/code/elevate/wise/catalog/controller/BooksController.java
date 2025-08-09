package com.code.elevate.wise.catalog.controller;


import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import com.code.elevate.wise.catalog.service.BooksService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("books")
@Log4j2
public class BooksController {

    @Autowired private BooksService service;

    @GetMapping
    public ResponseEntity<List<BookEntity>> getAllBooks(){
       log.info("test get all");
        return ResponseEntity.ok(service.findAllBooks());
    }
}
