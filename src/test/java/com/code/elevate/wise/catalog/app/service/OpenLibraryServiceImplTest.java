package com.code.elevate.wise.catalog.app.service;

import com.code.elevate.wise.catalog.app.repository.BooksRepository;
import com.code.elevate.wise.catalog.app.service.impl.OpenLibraryServiceImpl;
import com.code.elevate.wise.catalog.domain.dto.SubjectDTO;
import com.code.elevate.wise.catalog.domain.dto.WorkDTO;
import com.code.elevate.wise.catalog.domain.entity.BookEntity;
import com.code.elevate.wise.catalog.domain.mapper.OpenLibraryMapper;
import com.code.elevate.wise.catalog.infra.client.OpenLibraryClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
class OpenLibraryServiceImplTest {

    @Mock private OpenLibraryClient client;
    @Mock private BooksRepository repository;
    @Mock private OpenLibraryMapper mapper;
    @Mock private RedisTemplate<String, Object> redis;
    @Mock private RedisConnectionFactory connectionFactory;
    @Mock private RedisConnection connection;
    @InjectMocks private OpenLibraryServiceImpl service;

    @Test
    void createListOfBooks_ShouldSaveBooks_WhenClientReturnsData() {
        WorkDTO work = new WorkDTO();
        work.setTitle("Book One");

        SubjectDTO dto = new SubjectDTO();
        dto.setName("fantasy");
        dto.setWorks(List.of(work));

        when(client.getSubjectJson(anyString())).thenReturn(Mono.just(dto));

        BookEntity entity = new BookEntity();
        when(repository.findByTitle(anyString())).thenReturn(Optional.empty());
        when(mapper.toBookEntity(any(), anyString())).thenReturn(entity);

        service.createListOfBooks(List.of("fantasy"));

        verify(repository, atLeastOnce()).saveAll(anyList());
    }

    @Test
    void deleteListOfBooks_ShouldDeleteRepositoryAndFlushRedis() {
        when(redis.getConnectionFactory()).thenReturn(connectionFactory);
        when(connectionFactory.getConnection()).thenReturn(connection);

        service.deleteListOfBooks();

        verify(repository, times(1)).deleteAll();
        verify(connection, times(1)).flushDb();
    }
}

