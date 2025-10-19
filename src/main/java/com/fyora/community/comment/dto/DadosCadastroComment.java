package com.fyora.community.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosCadastroComment(
        // @NotNull Long communityUserId,
        @NotBlank @Size(max = 255) String content
) {}
