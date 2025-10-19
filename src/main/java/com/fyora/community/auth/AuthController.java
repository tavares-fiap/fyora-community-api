package com.fyora.community.auth;

import com.fyora.community.auth.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth") @Tag(name="Auth")
public class AuthController {

    private final AuthService service;
    public AuthController(AuthService service){ this.service = service; }

    @PostMapping("/register")
    @Operation(summary = "Registra usuário (BCrypt)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cadastro realizado com sucesso!"),
            @ApiResponse(responseCode = "409", description = "Usuário já cadastrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    public ResponseEntity<Void> register(@RequestBody @Validated RegisterRequest req){
        service.register(req);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica e retorna JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso!"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    public ResponseEntity<TokenResponse> login(@RequestBody @Validated LoginRequest req){
        String jwt = service.login(req);
        return ResponseEntity.ok(new TokenResponse(jwt));
    }

}
