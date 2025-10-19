package com.fyora.community.post;

import com.fyora.community.comment.dto.DadosCadastroComment;
import com.fyora.community.comment.dto.DadosListagemComment;
import com.fyora.community.post.dto.DadosCadastroPost;
import com.fyora.community.post.dto.DadosDetalhePost;
import com.fyora.community.post.dto.DadosListagemPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostServiceInterface {
    
    /**
     * Cria um novo post na comunidade
     * @param dto dados do post a ser criado
     * @return detalhes do post criado
     */
    DadosDetalhePost criar(DadosCadastroPost dto);
    
    /**
     * Apoia um post específico
     * @param postId ID do post a ser apoiado
     */
    void apoiar(Long postId);
    
    /**
     * Remove o apoio de um post específico
     * @param postId ID do post
     */
    void desfazerApoio(Long postId);
    
    /**
     * Comenta em um post específico
     * @param postId ID do post
     * @param dto dados do comentário
     * @return detalhes do comentário criado
     */
    DadosListagemComment comment(Long postId, DadosCadastroComment dto);
    
    /**
     * Retorna o feed de posts paginado
     * @param pageable configurações de paginação
     * @return página de posts
     */
    Page<DadosListagemPost> feed(Pageable pageable);
    
    /**
     * Retorna os comentários de um post específico
     * @param id ID do post
     * @param pageable configurações de paginação
     * @return página de comentários
     */
    Page<DadosListagemComment> commentFeed(Long id, Pageable pageable);
}

