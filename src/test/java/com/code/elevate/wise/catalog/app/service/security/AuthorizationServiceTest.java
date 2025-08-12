package com.code.elevate.wise.catalog.app.service.security;

import com.code.elevate.wise.catalog.app.repository.UserRepository;
import com.code.elevate.wise.catalog.app.service.security.AuthorizationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService service;

    @Mock
    private UserRepository repository;

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        String username = "user";
        UserDetails user = mock(UserDetails.class);
        when(repository.findByLogin(username)).thenReturn(user);

        UserDetails result = service.loadUserByUsername(username);

        assertEquals(user, result);
        verify(repository).findByLogin(username);
    }
}
