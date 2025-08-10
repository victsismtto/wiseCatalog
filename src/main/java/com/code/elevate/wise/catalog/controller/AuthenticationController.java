package com.code.elevate.wise.catalog.controller;

import com.code.elevate.wise.catalog.domain.dto.AuthenticationDTO;
import com.code.elevate.wise.catalog.domain.dto.LoginResponseDTO;
import com.code.elevate.wise.catalog.domain.dto.RegisterDTO;
import com.code.elevate.wise.catalog.domain.entity.UserEntity;
import com.code.elevate.wise.catalog.repository.UserRepository;
import com.code.elevate.wise.catalog.service.security.TokenService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@Log4j2
@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data){
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((UserEntity) auth.getPrincipal());
        log.info("token generated");
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data){
        if(this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserEntity newUser = new UserEntity(data.login(), encryptedPassword, data.role());
        this.repository.save(newUser);
        log.info("user saved in the database");
        return ResponseEntity.ok().build();
    }
}
