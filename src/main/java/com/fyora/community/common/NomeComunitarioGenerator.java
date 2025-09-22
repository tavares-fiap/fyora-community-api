package com.fyora.community.common;

import com.fyora.community.communityuser.CommunityUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class NomeComunitarioGenerator {

    private final CommunityUserRepository repository;
    private static final String[] ADJETIVOS = {"Fênix", "Luz", "Brisa", "Aurora", "Sombra", "Estrela"};
    private static final String[] SUBSTANTIVOS = {"Curiosa", "Serena", "Valente", "Atenta", "Brilhante", "Silenciosa"};
    private static final SecureRandom RND = new SecureRandom();

    /** Gera nome único com tentativas limitadas (boa prática para unicidade) */
    public String gerarUnico() {
        for (int i = 0; i < 20; i++) {
            String nome = ADJETIVOS[RND.nextInt(ADJETIVOS.length)] + " " +
                    SUBSTANTIVOS[RND.nextInt(SUBSTANTIVOS.length)] + " #" +
                    (1000 + RND.nextInt(9000));
            if (!repository.existsByCommunityName(nome)) return nome;
        }
        // fallback improvável
        return "Fênix " + System.currentTimeMillis();
    }
}
