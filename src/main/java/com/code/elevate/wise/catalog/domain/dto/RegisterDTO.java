package com.code.elevate.wise.catalog.domain.dto;

import com.code.elevate.wise.catalog.domain.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotBlank(message = "O login é obrigatório")
        String login,
        @NotBlank(message = "O login é obrigatório")
        @Size(min = 6, message = "deve ter pelo menos 6 caracteres")
        String password,
        @NotBlank(message = "A role é obrigatório")
        UserRole role
) {}
