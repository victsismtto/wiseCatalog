package com.code.elevate.wise.catalog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private String id;
    private String title;
    private String author;
    private int publicationYear;
    private String genre;
    private String subgenre;
}
