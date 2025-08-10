package com.code.elevate.wise.catalog.domain.dto;

import com.code.elevate.wise.catalog.domain.entity.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
