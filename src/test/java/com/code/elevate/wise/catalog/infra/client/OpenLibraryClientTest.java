package com.code.elevate.wise.catalog.infra.client;

import com.code.elevate.wise.catalog.domain.exception.NotFoundException;
import com.code.elevate.wise.catalog.domain.exception.ServiceUnavailableException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

class OpenLibraryClientTest {

    private MockWebServer server;
    private OpenLibraryClient client;

    @BeforeEach
    void setUp() throws Exception {
        server = new MockWebServer();
        server.start();

        String baseUrl = server.url("/").toString();
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();

        client = new OpenLibraryClient();
        ReflectionTestUtils.setField(client, "webClient", webClient);
    }

    @AfterEach
    void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    void getSubjectJson_success() {
        String body = "{\"name\":\"Fantasy\"}";

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(body));

        StepVerifier.create(client.getSubjectJson("fantasy.json"))
                .expectNextMatches(dto -> "Fantasy".equals(dto.getName()))
                .verifyComplete();
    }

    @Test
    void getSubjectJson_notFound() {
        server.enqueue(new MockResponse()
                .setResponseCode(404)
                .addHeader("Content-Type", "application/json")
                .setBody("{\"error\":\"not found\"}"));

        StepVerifier.create(client.getSubjectJson("invalid.json"))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void getSubjectJson_serviceUnavailable() {
        server.enqueue(new MockResponse()
                .setResponseCode(503)
                .addHeader("Content-Type", "application/json")
                .setBody("{\"error\":\"unavailable\"}"));

        StepVerifier.create(client.getSubjectJson("anything.json"))
                .expectError(ServiceUnavailableException.class)
                .verify();
    }
}


