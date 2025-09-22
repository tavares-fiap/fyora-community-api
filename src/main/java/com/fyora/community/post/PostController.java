package com.fyora.community.post;

import com.fyora.community.comment.dto.DadosCadastroComment;
import com.fyora.community.comment.dto.DadosListagemComment;
import com.fyora.community.post.dto.DadosCadastroPost;
import com.fyora.community.post.dto.DadosDetalhePost;
import com.fyora.community.post.dto.DadosListagemPost;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @PostMapping
    public ResponseEntity<DadosDetalhePost> criar(@RequestBody @Valid DadosCadastroPost dto) {
        return ResponseEntity.status(201).body(service.criar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemPost>> feed(Pageable pageable) {
        return ResponseEntity.ok(service.feed(pageable));
    }

    @PostMapping("/{id}/support")
    public ResponseEntity<Void> apoiar(@PathVariable Long id, @RequestParam Long userId) {
        service.apoiar(id, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/support")
    public ResponseEntity<Void> desfazerApoio(@PathVariable Long id, @RequestParam Long userId) {
        service.desfazerApoio(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<DadosListagemComment> comment(
            @PathVariable Long id,
            @RequestBody @Valid DadosCadastroComment dto) {
        return ResponseEntity.status(201).body(service.comment(id, dto));
    }
}
