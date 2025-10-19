package com.fyora.community.post;

import com.fyora.community.comment.Comment;
import com.fyora.community.comment.CommentRepository;
import com.fyora.community.comment.dto.DadosCadastroComment;
import com.fyora.community.comment.dto.DadosListagemComment;
import com.fyora.community.common.exception.BusinessException;
import com.fyora.community.common.exception.ResourceNotFoundException;
import com.fyora.community.communityuser.CommunityUser;
import com.fyora.community.communityuser.CommunityUserService;
import com.fyora.community.post.dto.DadosCadastroPost;
import com.fyora.community.post.dto.DadosDetalhePost;
import com.fyora.community.post.dto.DadosListagemPost;
import com.fyora.community.tag.Tag;
import com.fyora.community.tag.TagRepository;
import com.fyora.community.tag.TagType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService implements PostServiceInterface {

    private final PostRepository postRepository;
    private final SupportRepository supportRepository;
    private final TagRepository tagRepository;
    private final CommentRepository comentarioRepository;
    private final CommunityUserService userService;
    private final com.fyora.community.infra.security.CurrentUserService currentUser;

    @Transactional
    public DadosDetalhePost criar(DadosCadastroPost dto) {
        // Validações de negócio
        validatePostContent(dto.content());
        
        // pega o user logado -> communityUser associado (cria se não existir)
        var account = currentUser.requireUserAccount();
        CommunityUser author = userService.getOrCreateByAccount(account);

        Post p = new Post();
        p.setContent(dto.content());
        p.setCommunityUser(author);

        if (dto.tags() != null && !dto.tags().isEmpty()) {
            validateAndSetTags(p, dto.tags());
        }

        postRepository.save(p);
        return new DadosDetalhePost(
                p.getId(), p.getContent(), p.getCreatedAt(),
                author.getCommunityName(),
                p.getTags().stream().map(t -> t.getType().name()).collect(Collectors.toSet()),
                p.getSupportsCount()
        );
    }

    @Transactional
    public void apoiar(Long postId) {
        var account = currentUser.requireUserAccount();
        CommunityUser actor = userService.getOrCreateByAccount(account);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (supportRepository.existsByPostIdAndCommunityUserId(postId, actor.getId())) {
            throw new BusinessException("Usuário já apoiou este post");
        }

        Support s = new Support();
        s.setPost(post);
        s.setCommunityUser(actor);
        supportRepository.save(s);

        post.setSupportsCount(post.getSupportsCount() + 1);
        postRepository.save(post);
    }

    @Transactional
    public void desfazerApoio(Long postId) {
        var account = currentUser.requireUserAccount();
        CommunityUser actor = userService.getOrCreateByAccount(account);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        long before = supportRepository.countByPostId(postId);
        supportRepository.deleteByPostIdAndCommunityUserId(postId, actor.getId());
        long after = supportRepository.countByPostId(postId);

        if (after != before) {
            post.setSupportsCount((int) after);
            postRepository.save(post);
        }
    }

    @Transactional
    public DadosListagemComment comment(Long postId, DadosCadastroComment dto) {
        var account = currentUser.requireUserAccount();
        CommunityUser author = userService.getOrCreateByAccount(account);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Comment c = new Comment();
        c.setContent(dto.content());
        c.setPost(post);
        c.setCommunityUser(author);
        comentarioRepository.save(c);

        return new DadosListagemComment(c.getId(), c.getContent(), c.getCreatedAt(), author.getCommunityName());
    }

    @Transactional(readOnly = true)
    public Page<DadosListagemPost> feed(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(p -> new DadosListagemPost(
                        p.getId(), p.getContent(), p.getCreatedAt(),
                        p.getCommunityUser().getCommunityName(),
                        p.getTags().stream().map(t -> t.getType().name()).collect(Collectors.toSet()),
                        p.getSupportsCount()
                ));
    }

    @Transactional(readOnly = true)
    public Page<DadosListagemComment> commentFeed(Long id, Pageable pageable) {
        return comentarioRepository.findByPostIdOrderByCreatedAtDesc(id, pageable)
                .map(c -> new DadosListagemComment(
                        c.getId(), c.getContent(), c.getCreatedAt(),
                        c.getCommunityUser().getCommunityName()
                ));
    }

    /**
     * Valida o conteúdo do post conforme regras de negócio
     */
    private void validatePostContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new BusinessException("Conteúdo do post não pode estar vazio");
        }
        if (content.length() > 1000) {
            throw new BusinessException("Conteúdo do post não pode exceder 1000 caracteres");
        }
        // Validação adicional: não permitir apenas espaços em branco
        if (content.trim().length() < 3) {
            throw new BusinessException("Conteúdo do post deve ter pelo menos 3 caracteres");
        }
    }

    /**
     * Valida e define as tags do post
     */
    private void validateAndSetTags(Post post, Set<String> tagNames) {
        if (tagNames.size() > 5) {
            throw new BusinessException("Um post não pode ter mais de 5 tags");
        }
        
        Set<Tag> tags = tagNames.stream()
                .map(s -> {
                    try {
                        return TagType.valueOf(s.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new BusinessException("Tag inválida: " + s);
                    }
                })
                .map(tt -> tagRepository.findByType(tt)
                        .orElseThrow(() -> new BusinessException("Tag não encontrada: " + tt)))
                .collect(Collectors.toSet());
        
        tags.forEach(post::addTag);
    }
}
