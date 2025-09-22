package com.fyora.community.communityuser;

import com.fyora.community.communityuser.dto.DadosListagemCommunityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/users")
@RequiredArgsConstructor
public class CommunityUserController {

    private final CommunityUserService service;

    @PostMapping
    public ResponseEntity<DadosListagemCommunityUser> criar() {
        return ResponseEntity.ok(service.criar());
    }
}
