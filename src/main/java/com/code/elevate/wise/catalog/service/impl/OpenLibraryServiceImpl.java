package com.code.elevate.wise.catalog.service.impl;

import com.code.elevate.wise.catalog.client.OpenLibraryClient;
import com.code.elevate.wise.catalog.domain.dto.SubjectDTO;
import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import com.code.elevate.wise.catalog.mapper.OpenLibraryMapper;
import com.code.elevate.wise.catalog.repository.BooksRepository;
import com.code.elevate.wise.catalog.service.OpenLibraryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.List;

@Log4j2
@Service
public class OpenLibraryServiceImpl implements OpenLibraryService {

    @Autowired private OpenLibraryClient client;
    @Autowired private BooksRepository repository;
    @Autowired private OpenLibraryMapper mapper;
    private static final  List<String> GENRE_LIST = List.of("triller.json", "comedy.json", "romance.json", "action.json", "fantasy.json");
    @Override
    public void createListOfBooks() {
        GENRE_LIST.parallelStream().forEach(element -> {
            Mono<SubjectDTO> response = client.getSubjectJson(element);
            List<BookEntity> bookEntities = getEntityList(response).block();
            log.info(bookEntities);
            if (bookEntities != null && !bookEntities.isEmpty())  {
                repository.saveAll(bookEntities);
            }
        });

    }

    public Mono<List<BookEntity>> getEntityList(Mono<SubjectDTO> subjectDTOMono) {
        return subjectDTOMono
                .map(subjectDTO -> {
                    String genre = subjectDTO.getName();
                    return subjectDTO.getWorks()
                            .stream()
                            .map(work -> mapper.toBookEntity(work, genre))
                            .toList();
                });
    }
}
