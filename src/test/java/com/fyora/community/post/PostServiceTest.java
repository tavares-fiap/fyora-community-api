package com.fyora.community.post;

import com.fyora.community.auth.UserAccount;
import com.fyora.community.comment.Comment;
import com.fyora.community.comment.CommentRepository;
import com.fyora.community.comment.dto.DadosCadastroComment;
import com.fyora.community.comment.dto.DadosListagemComment;
import com.fyora.community.common.exception.BusinessException;
import com.fyora.community.common.exception.ResourceNotFoundException;
import com.fyora.community.communityuser.CommunityUser;
import com.fyora.community.communityuser.CommunityUserService;
import com.fyora.community.infra.security.CurrentUserService;
import com.fyora.community.post.dto.DadosCadastroPost;
import com.fyora.community.post.dto.DadosDetalhePost;
import com.fyora.community.post.dto.DadosListagemPost;
import com.fyora.community.tag.Tag;
import com.fyora.community.tag.TagRepository;
import com.fyora.community.tag.TagType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock private PostRepository postRepository;
    @Mock private SupportRepository supportRepository;
    @Mock private TagRepository tagRepository;
    @Mock private CommentRepository commentRepository;
    @Mock private CommunityUserService userService;
    @Mock private CurrentUserService currentUser;

    @InjectMocks private PostService postService;

    private UserAccount account;
    private CommunityUser author;
    private Post post;

    @BeforeEach
    void setup() {
        account = new UserAccount();
        account.setId(7L);
        account.setUsername("val");
        account.setRole("USER");

        author = new CommunityUser();
        author.setId(17L);
        author.setCommunityName("Fênix Val");
        author.setUserAccount(account);

        post = new Post();
        post.setId(1L);
        post.setContent("conteúdo de teste");
        post.setCreatedAt(LocalDateTime.now());
        post.setSupportsCount(0);
        post.setCommunityUser(author);
    }

    // ===== CRIAR POST =====

    @Test
    void criar_DeveCriarSemTagsERetornarDTO() {
        when(currentUser.requireUserAccount()).thenReturn(account);
        when(userService.getOrCreateByAccount(account)).thenReturn(author);
        when(postRepository.save(any(Post.class))).thenAnswer(inv -> {
            Post p = inv.getArgument(0);
            p.setId(10L);
            p.setCreatedAt(LocalDateTime.now());
            return p;
        });

        var in = new DadosCadastroPost("olá mundo", null);
        DadosDetalhePost out = postService.criar(in);

        assertNotNull(out);
        assertEquals(10L, out.id());
        assertEquals("olá mundo", out.content());
        assertEquals("Fênix Val", out.authorName());
        assertEquals(0, out.supportsCount());
        assertTrue(out.tags().isEmpty());
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void criar_ComTagsValidas_DeveResolverPorEnumEAssociar() {
        when(currentUser.requireUserAccount()).thenReturn(account);
        when(userService.getOrCreateByAccount(account)).thenReturn(author);

        // entrada em String -> service faz valueOf(UPPER) -> busca no repo
        Set<String> nomes = Set.of("vitoria", "DESABAFO");
        Tag t1 = tag(100L, TagType.VITORIA);
        Tag t2 = tag(101L, TagType.DESABAFO);

        when(tagRepository.findByType(TagType.VITORIA)).thenReturn(Optional.of(t1));
        when(tagRepository.findByType(TagType.DESABAFO)).thenReturn(Optional.of(t2));

        when(postRepository.save(any(Post.class))).thenAnswer(inv -> {
            Post p = inv.getArgument(0);
            p.setId(11L);
            p.setCreatedAt(LocalDateTime.now());
            return p;
        });

        var in = new DadosCadastroPost("com tags", nomes);
        var out = postService.criar(in);

        assertEquals(11L, out.id());
        assertEquals(Set.of("VITORIA", "DESABAFO"), out.tags());
        verify(tagRepository).findByType(TagType.VITORIA);
        verify(tagRepository).findByType(TagType.DESABAFO);
    }

    @Test
    void criar_ComTagStringInvalida_DeveLancarBusinessException() {
        // conteúdo VÁLIDO para passar da validação de conteúdo
        when(currentUser.requireUserAccount()).thenReturn(new UserAccount()); // usado depois que passa conteúdo
        when(userService.getOrCreateByAccount(any())).thenReturn(new CommunityUser());

        var in = new DadosCadastroPost("conteudo valido", Set.of("??invalid??"));

        BusinessException ex = assertThrows(BusinessException.class, () -> postService.criar(in));
        assertTrue(ex.getMessage().contains("Tag inválida")); // vem de validateAndSetTags
        verify(postRepository, never()).save(any());
    }


    // 2) Tag enum não encontrada no repositório
    @Test
    void criar_ComTagEnumNaoEncontradaNoRepositorio_DeveLancarBusinessException() {
        when(currentUser.requireUserAccount()).thenReturn(new UserAccount());
        when(userService.getOrCreateByAccount(any())).thenReturn(new CommunityUser());

        // valueOf("GATILHOS") é válido, mas o repo devolve Optional.empty()
        when(tagRepository.findByType(TagType.GATILHOS)).thenReturn(Optional.empty());

        var in = new DadosCadastroPost("conteudo valido", Set.of("gatilhos"));

        BusinessException ex = assertThrows(BusinessException.class, () -> postService.criar(in));
        assertTrue(ex.getMessage().contains("Tag não encontrada")); // "Tag não encontrada: GATILHOS"
        verify(postRepository, never()).save(any());
    }


    // 3) Mais de 5 tags
    @Test
    void criar_ComMaisDeCincoTags_DeveLancarBusinessException() {
        when(currentUser.requireUserAccount()).thenReturn(new UserAccount());
        when(userService.getOrCreateByAccount(any())).thenReturn(new CommunityUser());

        var in = new DadosCadastroPost(
                "conteudo valido",
                Set.of("A","B","C","D","E","F") // 6 tags
        );

        BusinessException ex = assertThrows(BusinessException.class, () -> postService.criar(in));
        assertTrue(ex.getMessage().contains("não pode ter mais de 5 tags")); // "Um post não pode ter mais de 5 tags"
        verify(postRepository, never()).save(any());
    }


    @Test
    void criar_ComConteudoVazioOuCurto_DeveLancarBusinessException() {
        assertThrows(BusinessException.class, () -> postService.criar(new DadosCadastroPost("  ", null)));
        assertThrows(BusinessException.class, () -> postService.criar(new DadosCadastroPost("aa", null)));
        verifyNoInteractions(postRepository, tagRepository, commentRepository, supportRepository, currentUser, userService);
    }



    // ===== APOIAR / DESFAZER APOIO =====

    @Test
    void apoiar_PrimeiraVez_DeveSalvarSupportEIncrementarContagem() {
        when(currentUser.requireUserAccount()).thenReturn(account);
        when(userService.getOrCreateByAccount(account)).thenReturn(author);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(supportRepository.existsByPostIdAndCommunityUserId(1L, author.getId())).thenReturn(false);

        postService.apoiar(1L);

        verify(supportRepository).save(any(Support.class));
        verify(postRepository, atLeastOnce()).save(argThat(p -> p.getSupportsCount() == 1));
    }

    @Test
    void apoiar_DuasVezes_DeveLancarBusinessException() {
        when(currentUser.requireUserAccount()).thenReturn(account);
        when(userService.getOrCreateByAccount(account)).thenReturn(author);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(supportRepository.existsByPostIdAndCommunityUserId(1L, author.getId())).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> postService.apoiar(1L));
        assertTrue(ex.getMessage().contains("Usuário já apoiou"));
        verify(supportRepository, never()).save(any());
    }

    @Test
    void desfazerApoio_QuandoRemove_DeveAtualizarContagem() {
        when(currentUser.requireUserAccount()).thenReturn(account);
        when(userService.getOrCreateByAccount(account)).thenReturn(author);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        when(supportRepository.countByPostId(1L)).thenReturn(1L, 0L); // before=1, after=0
        doNothing().when(supportRepository).deleteByPostIdAndCommunityUserId(1L, author.getId());

        postService.desfazerApoio(1L);

        verify(postRepository).save(argThat(p -> p.getSupportsCount() == 0));
    }

    @Test
    void desfazerApoio_QuandoNaoRemove_NaoAtualizaContagem() {
        when(currentUser.requireUserAccount()).thenReturn(account);
        when(userService.getOrCreateByAccount(account)).thenReturn(author);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        when(supportRepository.countByPostId(1L)).thenReturn(0L, 0L); // before=0, after=0
        doNothing().when(supportRepository).deleteByPostIdAndCommunityUserId(1L, author.getId());

        postService.desfazerApoio(1L);

        verify(postRepository, never()).save(any());
    }

    // ===== COMENTÁRIOS =====

    @Test
    void comment_DeveCriarComentarioERetornarDTO() {
        when(currentUser.requireUserAccount()).thenReturn(account);
        when(userService.getOrCreateByAccount(account)).thenReturn(author);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> {
            Comment c = inv.getArgument(0);
            c.setId(55L);
            c.setCreatedAt(LocalDateTime.now());
            return c;
        });

        var dto = new DadosCadastroComment("oi, val aqui");
        DadosListagemComment out = postService.comment(1L, dto);

        assertEquals(55L, out.id());
        assertEquals("oi, val aqui", out.content());
        assertEquals("Fênix Val", out.authorName());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void comment_ComPostInexistente_DeveLancarNotFound() {
        when(currentUser.requireUserAccount()).thenReturn(account);
        when(userService.getOrCreateByAccount(account)).thenReturn(author);
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> postService.comment(99L, new DadosCadastroComment("x")));
    }

    // ===== FEEDS =====

    @Test
    void feed_DeveMapearParaDTO_ComTagsVindasDoEnum() {
        // adiciona uma Tag VITORIA ao post
        Tag t = tag(1L, TagType.VITORIA);
        post.getTags().add(t);

        Page<Post> page = new PageImpl<>(List.of(post));
        when(postRepository.findAllByOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(page);

        Page<DadosListagemPost> out = postService.feed(PageRequest.of(0, 10));

        assertEquals(1, out.getTotalElements());
        var dto = out.getContent().get(0);
        assertEquals(post.getId(), dto.id());
        assertEquals("Fênix Val", dto.authorName());
        assertEquals(Set.of("VITORIA"), dto.tags());
    }

    @Test
    void commentFeed_DeveMapearParaDTO() {
        Comment c = new Comment();
        c.setId(9L);
        c.setContent("oi");
        c.setCommunityUser(author);
        c.setCreatedAt(LocalDateTime.now());

        Page<Comment> page = new PageImpl<>(List.of(c));
        when(commentRepository.findByPostIdOrderByCreatedAtDesc(eq(1L), any(Pageable.class))).thenReturn(page);

        Page<DadosListagemComment> out = postService.commentFeed(1L, PageRequest.of(0, 10));

        assertEquals(1, out.getTotalElements());
        assertEquals("oi", out.getContent().get(0).content());
        assertEquals("Fênix Val", out.getContent().get(0).authorName());
    }

    // ===== helpers =====
    private static Tag tag(Long id, TagType type) {
        Tag t = new Tag();
        t.setId(id);
        t.setType(type);
        // posts é um Set<Post>, mas não precisamos preenchê-lo para estes testes
        return t;
    }
}
