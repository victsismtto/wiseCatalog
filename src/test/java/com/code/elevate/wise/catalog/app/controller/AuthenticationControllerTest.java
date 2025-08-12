package com.code.elevate.wise.catalog.app.controller;

import com.code.elevate.wise.catalog.domain.dto.AuthenticationDTO;
import com.code.elevate.wise.catalog.domain.dto.LoginResponseDTO;
import com.code.elevate.wise.catalog.domain.dto.RegisterDTO;
import com.code.elevate.wise.catalog.domain.entity.UserRole;
import com.code.elevate.wise.catalog.app.service.security.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {
    @InjectMocks
    private AuthenticationController controller;
    @Mock private AuthenticationService service;

    @Test
    void loginTest() {
        AuthenticationDTO data = new AuthenticationDTO("login", "password");
        ResponseEntity<LoginResponseDTO> login = controller.login(data);
        Assertions.assertTrue(login.getStatusCode().is2xxSuccessful());
    }

    @Test
    void registerTest() {
        RegisterDTO data = new RegisterDTO("login", "password", UserRole.ADMIN);
        Assertions.assertDoesNotThrow(() -> controller.register(data));
    }
}
