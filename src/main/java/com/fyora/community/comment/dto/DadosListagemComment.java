package com.fyora.community.comment.dto;

import java.time.LocalDateTime;

public record DadosListagemComment(
        Long id,
        String content,
        LocalDateTime createdAt,
        String authorName
) {}
