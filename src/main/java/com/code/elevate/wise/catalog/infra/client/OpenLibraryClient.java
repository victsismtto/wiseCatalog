package com.code.elevate.wise.catalog.infra.client;

import com.code.elevate.wise.catalog.domain.dto.SubjectDTO;
import com.code.elevate.wise.catalog.domain.exception.NotFoundException;
import com.code.elevate.wise.catalog.domain.exception.ServiceUnavailableException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class OpenLibraryClient {

    @Autowired private WebClient webClient;

    public Mono<SubjectDTO> getSubjectJson(String uri) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        resp -> Mono.error(new NotFoundException("There is no endpoint for OpenLibrary with this path"))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        resp -> Mono.error(new ServiceUnavailableException("The OpenLibrary service is temporarily unavailable"))
                )
                .bodyToMono(SubjectDTO.class);
    }
}
