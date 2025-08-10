package com.code.elevate.wise.catalog.client;

import com.code.elevate.wise.catalog.domain.dto.SubjectDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class OpenLibraryClient {

    @Autowired
    private WebClient webClient;

    public Mono<SubjectDTO> getSubjectJson(String uri) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(SubjectDTO.class);
    }
}
