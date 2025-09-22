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
public class PostService {

    private final PostRepository postRepository;
    private final SupportRepository supportRepository;
    private final TagRepository tagRepository;
    private final CommentRepository comentarioRepository;
    private final CommunityUserService userService;

    @Transactional
    public DadosDetalhePost criar(DadosCadastroPost dto) {
        CommunityUser author = userService.getById(dto.communityUserId());

        Post p = new Post();
        p.setContent(dto.content());
        p.setCommunityUser(author);

        if (dto.tags() != null && !dto.tags().isEmpty()) {
            Set<Tag> tags = dto.tags().stream()
                    .map(s -> TagType.valueOf(s.toUpperCase()))
                    .map(tt -> tagRepository.findByType(tt)
                            .orElseThrow(() -> new BusinessException("Tag inválida: " + tt)))
                    .collect(Collectors.toSet());
            tags.forEach(p::addTag);
        }

        postRepository.save(p);
        return new DadosDetalhePost(p.getId(), p.getContent(), p.getCreatedAt(),
                author.getCommunityName(),
                p.getTags().stream().map(t -> t.getType().name()).collect(Collectors.toSet()),
                p.getSupportsCount());
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

    @Transactional
    public void apoiar(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        userService.getById(userId);

        if (supportRepository.existsByPostIdAndCommunityUserId(postId, userId)) {
            throw new BusinessException("Usuário já apoiou este post");
        }

        Support s = new Support();
        s.setPost(post);
        s.setCommunityUser(new CommunityUser()); // proxy leve
        s.getCommunityUser().setId(userId);
        supportRepository.save(s);

        post.setSupportsCount(post.getSupportsCount() + 1);
        postRepository.save(post);
    }

    @Transactional
    public void desfazerApoio(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        long before = supportRepository.countByPostId(postId);

        supportRepository.deleteByPostIdAndCommunityUserId(postId, userId);

        long after = supportRepository.countByPostId(postId);
        if (after != before) {
            post.setSupportsCount((int) after);
            postRepository.save(post);
        }
    }

    @Transactional
    public DadosListagemComment comment(Long postId, DadosCadastroComment dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        CommunityUser author = userService.getById(dto.communityUserId());

        Comment c = new Comment();
        c.setContent(dto.content());
        c.setPost(post);
        c.setCommunityUser(author);
        comentarioRepository.save(c);

        return new DadosListagemComment(c.getId(), c.getContent(), c.getCreatedAt(), author.getCommunityName());
    }
}
