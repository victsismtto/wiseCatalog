package com.code.elevate.wise.catalog.client;

import com.code.elevate.wise.catalog.domain.dto.SubjectDTO;
import com.code.elevate.wise.catalog.exception.NotFoundException;
import com.code.elevate.wise.catalog.exception.ServiceUnavailableException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class OpenLibraryClient {

    @Autowired
    private WebClient webClient;

    public Mono<SubjectDTO> getSubjectJson(String uri) {
        try {
            return webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(SubjectDTO.class);
        } catch (HttpClientErrorException e) {
            throw new NotFoundException("There is no endpoint for openLibrary with this path");
        } catch (Exception e) {
            throw new ServiceUnavailableException("the openTelemetry service is temporally unavailable");
        }

    }
}
