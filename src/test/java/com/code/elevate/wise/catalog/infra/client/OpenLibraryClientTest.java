package com.code.elevate.wise.catalog.infra.client;

import com.code.elevate.wise.catalog.domain.dto.SubjectDTO;
import com.code.elevate.wise.catalog.domain.exception.NotFoundException;
import com.code.elevate.wise.catalog.domain.exception.ServiceUnavailableException;
import com.code.elevate.wise.catalog.infra.client.OpenLibraryClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenLibraryClientTest {

    @InjectMocks
    private OpenLibraryClient client;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> uriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> headersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Test
    void getSubjectJson_shouldReturnMonoSubjectDTO_whenSuccess() {
        String uri = "fantasy.json";
        SubjectDTO expectedSubject = new SubjectDTO();
        expectedSubject.setName("Fantasy");

        // Usando doAnswer para burlar o problema de tipo
        doAnswer(invocation -> uriSpec).when(webClient).get();
        doAnswer(invocation -> headersSpec).when(uriSpec).uri(uri);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(SubjectDTO.class)).thenReturn(Mono.just(expectedSubject));

        StepVerifier.create(client.getSubjectJson(uri))
                .expectNextMatches(subject -> "Fantasy".equals(subject.getName()))
                .verifyComplete();
    }

    @Test
    void getSubjectJson_shouldThrowNotFoundException_whenHttpClientErrorException() {
        String uri = "invalid.json";

        doAnswer(invocation -> uriSpec).when(webClient).get();
        doAnswer(invocation -> headersSpec).when(uriSpec).uri(uri);
        when(headersSpec.retrieve()).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Assertions.assertThrows(NotFoundException.class, () -> {
            client.getSubjectJson(uri);
        });
    }
}


