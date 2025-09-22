package com.fyora.community.post.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record DadosDetalhePost(
        Long id,
        String content,
        LocalDateTime createdAt,
        String authorName,
        Set<String> tags,
        Integer supportsCount
) {}
