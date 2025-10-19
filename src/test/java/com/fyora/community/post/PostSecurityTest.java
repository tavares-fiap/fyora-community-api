package com.fyora.community.post;

import com.fyora.community.auth.UserAccount;
import com.fyora.community.auth.UserAccountRepository;
import com.fyora.community.infra.security.JwtTokenProvider;
import com.fyora.community.infra.security.CurrentUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
@AutoConfigureMockMvc(addFilters = true)
class PostSecurityTest {

    @Autowired MockMvc mvc;

    @MockBean
    PostServiceInterface service;
    @MockBean
    UserAccountRepository userAccountRepository;

    @Test
    void criarSemToken_deve401() throws Exception {
        mvc.perform(post("/api/community/posts")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"content\":\"Este é um conteúdo válido para teste\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "tester", roles = "USER")
    void criarComToken_deve201() throws Exception {
        // Mock do UserAccount para o CurrentUserService
        UserAccount userAccount = new UserAccount();
        userAccount.setId(1L);
        userAccount.setUsername("tester");
        userAccount.setRole("USER");
        
        when(userAccountRepository.findByUsername("tester")).thenReturn(Optional.of(userAccount));
        
        // Mock da resposta do service
        when(service.criar(any())).thenReturn(
                new com.fyora.community.post.dto.DadosDetalhePost(
                        1L, "Este é um conteúdo válido para teste", 
                        java.time.LocalDateTime.now(), "Tester", 
                        java.util.Set.of(), 0
                )
        );

        mvc.perform(post("/api/community/posts")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"content\":\"Este é um conteúdo válido para teste\"}"))
                .andExpect(status().isCreated());
    }
}