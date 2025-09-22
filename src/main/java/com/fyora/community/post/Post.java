package com.fyora.community.post;

import com.fyora.community.comment.Comment;
import com.fyora.community.communityuser.CommunityUser;
import com.fyora.community.tag.Tag;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "supports_count", nullable = false)
    private Integer supportsCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_user_id", nullable = false) // coluna de chave estrangeira na tabela 'posts'
    private CommunityUser communityUser;

    // campo 'post' na entidade 'Comment' como proprietario
    // se um post é deletado, seus comentarios tambem serao
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    // um Post pode ter muitas tags
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_tags", // nome da tabela de juncao
            joinColumns = @JoinColumn(name = "post_id"), // coluna para o Post na tabela de juncao
            inverseJoinColumns = @JoinColumn(name = "tag_id") // coluna para a tag na tabela de juncao
    )
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Support> supports = new HashSet<>();

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getPosts().add(this); // adciona esse post a lista de posts da tag
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getPosts().remove(this);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setPost(null);
    }
}
