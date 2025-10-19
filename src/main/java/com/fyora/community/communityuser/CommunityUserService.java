package com.fyora.community.communityuser;

import com.fyora.community.common.NomeComunitarioGenerator;
import com.fyora.community.common.exception.ResourceNotFoundException;
import com.fyora.community.communityuser.dto.DadosListagemCommunityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommunityUserService {

    private final CommunityUserRepository repository;
    private final NomeComunitarioGenerator generator;

    @Transactional
    public DadosListagemCommunityUser criar() {
        CommunityUser u = new CommunityUser();
        u.setCommunityName(generator.gerarUnico());
        repository.save(u);
        return new DadosListagemCommunityUser(u.getId(), u.getCommunityName(), u.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public CommunityUser getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CommunityUser not found"));
    }

    @Transactional
    public CommunityUser getOrCreateByAccount(com.fyora.community.auth.UserAccount account) {
        return repository.findByUserAccountId(account.getId())
                .orElseGet(() -> {
                    CommunityUser u = new CommunityUser();
                    u.setCommunityName(generator.gerarUnico());
                    u.setUserAccount(account);
                    return repository.save(u);
                });
    }
}
