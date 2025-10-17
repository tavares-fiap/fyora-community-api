package com.fyora.community.communityuser;

import com.fyora.community.communityuser.dto.DadosListagemCommunityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// OpenAPI / Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/community/users")
@RequiredArgsConstructor
@Tag(name = "Community Users", description = "Endpoints para gerenciamento de usuários da comunidade.")
public class CommunityUserController {

    private final CommunityUserService service;

    @PostMapping
    @Operation(
            summary = "Cria usuário da comunidade",
            description = "Cria um usuário da comunidade e retorna seus dados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário criado"), // dica: considere retornar 201 Created no futuro
            @ApiResponse(responseCode = "409", description = "Conflito (usuário já existe)"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<DadosListagemCommunityUser> criar() {
        return ResponseEntity.ok(service.criar());
    }
}
