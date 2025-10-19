package com.fyora.community.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MeResponse", description = "Informações do usuário autenticado e do CommunityUser vinculado")
public record MeResponse(
        @Schema(example = "val") String username,
        @Schema(example = "USER") String role,
        @Schema(example = "42", description = "ID do CommunityUser (pode ser nulo se ainda não foi criado)") Long communityUserId,
        @Schema(example = "Fênix Curiosa #1234", description = "Nome comunitário (pode ser nulo se ainda não foi criado)") String communityName
) {}
