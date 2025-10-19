package com.fyora.community.auth.dto;

import jakarta.validation.constraints.NotBlank;
public record RegisterRequest(@NotBlank String username, @NotBlank String password) {}
