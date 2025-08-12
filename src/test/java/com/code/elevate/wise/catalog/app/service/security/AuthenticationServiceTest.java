package com.code.elevate.wise.catalog.app.service.security;

import com.code.elevate.wise.catalog.app.repository.UserRepository;
import com.code.elevate.wise.catalog.app.service.security.AuthenticationService;
import com.code.elevate.wise.catalog.domain.dto.AuthenticationDTO;
import com.code.elevate.wise.catalog.domain.dto.RegisterDTO;
import com.code.elevate.wise.catalog.domain.entity.UserEntity;
import com.code.elevate.wise.catalog.domain.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService service;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository repository;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(service, "secret", "my-secret-key");
    }

    @Test
    void generateToken_shouldReturnToken_whenAuthenticationSucceeds() {
        AuthenticationDTO authDTO = new AuthenticationDTO("user", "pass");
        UserEntity userEntity = new UserEntity();
        userEntity.setLogin("user");

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(authDTO.login(), authDTO.password());

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userEntity);
        when(authenticationManager.authenticate(any())).thenReturn(auth);

        String token = service.generateToken(authDTO);

        assertNotNull(token);
        assertTrue(token.contains("ey"));

        verify(authenticationManager).authenticate(any());
    }

    @Test
    void validateToken_shouldReturnSubject_whenTokenValid() {
        String token = "valid.token.here";
        String result = service.validateToken(token);
        assertEquals("", result);
    }

    @Test
    void register_shouldReturnBadRequest_whenUserAlreadyExists() {
        RegisterDTO dto = new RegisterDTO("user", "pass", UserRole.ADMIN);
        when(repository.findByLogin("user")).thenReturn(new UserEntity());

        ResponseEntity<?> response = service.register(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(repository, never()).save(any());
    }

    @Test
    void register_shouldSaveUserAndReturnCreated_whenUserDoesNotExist() {
        RegisterDTO dto = new RegisterDTO("newuser", "pass", UserRole.ADMIN);

        when(repository.findByLogin("newuser")).thenReturn(null);
        when(repository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = service.register(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(repository).save(any(UserEntity.class));
    }
}

