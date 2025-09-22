package com.fyora.community.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record DadosCadastroPost(
        @NotNull Long communityUserId,
        @NotBlank @Size(max = 500) String content,
        Set<@NotBlank String> tags
) {}
