package com.code.elevate.wise.catalog.service;

import com.code.elevate.wise.catalog.app.repository.BooksRepository;
import com.code.elevate.wise.catalog.app.service.impl.OpenLibraryServiceImpl;
import com.code.elevate.wise.catalog.domain.dto.SubjectDTO;
import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import com.code.elevate.wise.catalog.domain.mapper.OpenLibraryMapper;
import com.code.elevate.wise.catalog.infra.client.OpenLibraryClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OpenLibraryServiceImplTest {

    @InjectMocks
    private OpenLibraryServiceImpl service;

    @Mock
    private OpenLibraryClient client;

    @Mock
    private BooksRepository repository;

    @Mock
    private OpenLibraryMapper mapper;

    @Test
    void shouldCreateListOfBooks() {
        SubjectDTO.Work work = new SubjectDTO.Work();
        work.setTitle("Book Title");

        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setName("fantasy");
        subjectDTO.setWorks(List.of(work));

        when(client.getSubjectJson(anyString())).thenReturn(Mono.just(subjectDTO));
        when(repository.findByTitle(anyString())).thenReturn(Optional.empty());

        BookEntity entity = new BookEntity();
        entity.setTitle("Book Title");
        when(mapper.toBookEntity(any(), anyString())).thenReturn(entity);

        service.createListOfBooks();
        verify(repository, atLeastOnce()).saveAll(anyList());
    }

    @Test
    void shouldDeleteListOfBooks() {
        service.deleteListOfBooks();
        verify(repository).deleteAll();
    }
}
