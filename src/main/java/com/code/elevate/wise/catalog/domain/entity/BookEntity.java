package com.code.elevate.wise.catalog.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "books")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookEntity {
    @Id
    @Field("_id")
    private String _id;
    private String title;
    private String author;
    private int publicationYear;
    private String genre;
    private String subgenre;
}
