package com.fyora.community.post;

import com.fyora.community.comment.dto.DadosCadastroComment;
import com.fyora.community.comment.dto.DadosListagemComment;
import com.fyora.community.post.dto.DadosCadastroPost;
import com.fyora.community.post.dto.DadosDetalhePost;
import com.fyora.community.post.dto.DadosListagemPost;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// OpenAPI / Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/community/posts")
@RequiredArgsConstructor
@Tag(name = "Community Posts", description = "Endpoints para criação, listagem e interação (apoio/comentários) em posts da comunidade.")
public class PostController {

    private final PostService service;

    @PostMapping
    @Operation(
            summary = "Cria um post",
            description = "Cria um novo post na comunidade e retorna os detalhes do post criado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Post criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<DadosDetalhePost> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload para criação do post (título, conteúdo, etc.)", required = true
            )
            @org.springframework.web.bind.annotation.RequestBody @Valid DadosCadastroPost dto) {
        return ResponseEntity.status(201).body(service.criar(dto));
    }

    @GetMapping
    @Operation(
            summary = "Lista o feed de posts (paginado)",
            description = "Retorna uma página de posts ordenável. Use os parâmetros padrão do Spring Data: `page`, `size` e `sort`."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Feed retornado com sucesso")
    })
    public ResponseEntity<Page<DadosListagemPost>> feed(
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.feed(pageable));
    }

    @PostMapping("/{id}/support")
    @Operation(
            summary = "Apoiar um post",
            description = "Registra apoio (ex.: like) do usuário ao post."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Apoio registrado"),
            @ApiResponse(responseCode = "404", description = "Post ou usuário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Void> apoiar(
            @Parameter(description = "ID do post a apoiar", required = true)
            @PathVariable Long id,
            @Parameter(description = "ID do usuário que apoia", required = true, example = "123")
            @RequestParam Long userId) {
        service.apoiar(id, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/support")
    @Operation(
            summary = "Desfazer apoio em um post",
            description = "Remove o apoio do usuário previamente registrado no post."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Apoio removido"),
            @ApiResponse(responseCode = "404", description = "Post ou usuário não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Void> desfazerApoio(
            @Parameter(description = "ID do post", required = true)
            @PathVariable Long id,
            @Parameter(description = "ID do usuário que removerá o apoio", required = true, example = "123")
            @RequestParam Long userId) {
        service.desfazerApoio(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/comments")
    @Operation(
            summary = "Comentar em um post",
            description = "Cria um novo comentário em um post específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Comentário criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Post não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<DadosListagemComment> comment(
            @Parameter(description = "ID do post a ser comentado", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload do comentário (texto, metadados, etc.)", required = true
            )
            @org.springframework.web.bind.annotation.RequestBody @Valid DadosCadastroComment dto) {
        return ResponseEntity.status(201).body(service.comment(id, dto));
    }

}
