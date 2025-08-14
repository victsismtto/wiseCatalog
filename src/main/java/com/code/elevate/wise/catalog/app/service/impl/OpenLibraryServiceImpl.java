package com.code.elevate.wise.catalog.app.service.impl;

import com.code.elevate.wise.catalog.domain.dto.WorkDTO;
import com.code.elevate.wise.catalog.infra.client.OpenLibraryClient;
import com.code.elevate.wise.catalog.domain.dto.SubjectDTO;
import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import com.code.elevate.wise.catalog.domain.mapper.OpenLibraryMapper;
import com.code.elevate.wise.catalog.app.repository.BooksRepository;
import com.code.elevate.wise.catalog.app.service.OpenLibraryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
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
    @Autowired private RedisTemplate<String, Object> redis;
    @Override
    public void createListOfBooks(List<String> list) {
        Set<String> genreList = new HashSet<>(list.size());
        list.forEach(genre -> genreList.add(genre + ".json"));
        genreList.parallelStream().forEach(element -> {
            Mono<SubjectDTO> response = client.getSubjectJson(element);
            List<BookEntity> bookEntities = getEntityList(response).collectList().block();
            if (bookEntities != null && !bookEntities.isEmpty())  {
                repository.saveAll(bookEntities);
            }
        });
    }

    @Override
    public void deleteListOfBooks() {
        log.info("deleting the books");
        repository.deleteAll();
        redis.getConnectionFactory().getConnection().flushDb();
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

    private Predicate<WorkDTO> distinctByTitle() {
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
