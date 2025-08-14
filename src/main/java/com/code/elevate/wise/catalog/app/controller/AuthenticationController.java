package com.code.elevate.wise.catalog.app.controller;

import com.code.elevate.wise.catalog.domain.dto.AuthenticationDTO;
import com.code.elevate.wise.catalog.domain.dto.LoginResponseDTO;
import com.code.elevate.wise.catalog.domain.dto.RegisterDTO;
import com.code.elevate.wise.catalog.app.repository.UserRepository;
import com.code.elevate.wise.catalog.app.service.security.AuthenticationService;
import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import java.net.URI;

@Log4j2
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UserRepository repository;
    @Autowired private AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data){
        String token = service.generateToken(data);
        log.info("token generated");
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO data){
        return service.register(data);
    }
}
