package com.fyora.community.auth;


import com.fyora.community.auth.dto.MeResponse;
import com.fyora.community.communityuser.CommunityUserRepository;
import com.fyora.community.infra.security.CurrentUserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// OpenAPI / Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Autenticação e informações do usuário autenticado")
public class MeController {

    private final CurrentUserService current;
    private final CommunityUserRepository cuRepo;

    public MeController(CurrentUserService current, CommunityUserRepository cuRepo) {
        this.current = current;
        this.cuRepo = cuRepo;
    }

    @GetMapping("/me")
    @Operation(
            summary = "Retorna informações do usuário autenticado",
            description = "Exibe o username da conta, o papel (role) e, se já criado, o CommunityUser vinculado.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Informações do usuário autenticado",
                    content = @Content(schema = @Schema(implementation = MeResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado (token ausente ou inválido)",
                    content = @Content
            ),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor. Provavelmente falta de autenticacao!")
    })
    public ResponseEntity<MeResponse> me() {
        var acc = current.requireUserAccount();
        var cu  = cuRepo.findByUserAccountId(acc.getId()).orElse(null);

        var body = new MeResponse(
                acc.getUsername(),
                acc.getRole(),
                cu != null ? cu.getId() : null,
                cu != null ? cu.getCommunityName() : null
        );
        return ResponseEntity.ok(body);
    }
}