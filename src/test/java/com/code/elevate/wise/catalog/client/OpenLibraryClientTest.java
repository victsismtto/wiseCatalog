//package com.code.elevate.wise.catalog.client;
//
//import com.code.elevate.wise.catalog.domain.dto.SubjectDTO;
//import com.code.elevate.wise.catalog.infra.client.OpenLibraryClient;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class OpenLibraryClientTest {
//
//    @InjectMocks private OpenLibraryClient client;
//    @Mock
//    private WebClient webClient;
//
//    @Mock
//    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;
//
//    @Mock
//    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
//
//    @Mock
//    private WebClient.ResponseSpec responseSpec;
//
//
//    @Test
//    void getSubjectJsonTest() {
//        SubjectDTO dto = new SubjectDTO();
//        dto.setName("Fantasy");
//        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec) requestHeadersUriSpec);
////        when(requestHeadersUriSpec.uri(anyString())).thenReturn((WebClient.RequestHeadersSpec) requestHeadersSpec);
//        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(SubjectDTO.class)).thenReturn(Mono.just(dto));
//
//        StepVerifier.create(client.getSubjectJson("fantasy.json"))
//                .expectNextMatches(result -> result.getName().equals("Fantasy"))
//                .verifyComplete();
//
//        verify(webClient).get();
//        verify(requestHeadersUriSpec).uri("fantasy.json");
//    }
//
//}
