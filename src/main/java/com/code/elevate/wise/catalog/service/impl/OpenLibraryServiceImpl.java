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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@Log4j2
@Service
public class OpenLibraryServiceImpl implements OpenLibraryService {

    @Autowired private OpenLibraryClient client;
    @Autowired private BooksRepository repository;
    @Autowired private OpenLibraryMapper mapper;
    private static final  List<String> GENRE_LIST = List.of("triller.json", "comedy.json", "romance.json", "action.json", "fantasy.json");
    @Override
    public void createListOfBooks() {
        GENRE_LIST.forEach(element -> {
            Mono<SubjectDTO> response = client.getSubjectJson(element);
            List<BookEntity> bookEntities = getEntityList(response).collectList().block();
            log.info(bookEntities);
            if (bookEntities != null && !bookEntities.isEmpty())  {
                repository.saveAll(bookEntities);
            }
        });
    }

    @Override
    public void deleteListOfBooks() {
        repository.deleteAll();
    }

    private Flux<BookEntity> getEntityList(Mono<SubjectDTO> subjectDTOMono) {
        Mono<Flux<BookEntity>> fluxMono = subjectDTOMono
                .map(subjectDTO -> {
                    String genre = subjectDTO.getName();
                    return Flux.fromIterable(subjectDTO.getWorks().stream().filter(distinctByTitle()).toList())
                            .filter(work -> !existingTitles((work.getTitle())))
                            .map(work -> mapper.toBookEntity(work, genre));
                });
        return fluxMono.block();
    }

    private Predicate<SubjectDTO.Work> distinctByTitle() {
        Set<String> seen = ConcurrentHashMap.newKeySet();
        return work -> seen.add(work.getTitle());
    }

    private boolean existingTitles(String title) {
        Optional<BookEntity> optionalRepository = repository.findByTitle(title);
        if (optionalRepository.isEmpty()) {
            return false;
        }
        return repository.findByTitle(title).isPresent();
    }
}
