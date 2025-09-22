package com.fyora.community.communityuser;

import com.fyora.community.comment.Comment;
import com.fyora.community.post.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "community_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // usuario tem "nome de comunidade" generico para manter anonimato
    @Column(name = "community_name", nullable = false, unique = true)
    private String communityName;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // communityUser na entidade Post como proprietario do relacionamento
    // se um usuario é deletado, seus posts tambem sao
    // lista de posts só carrega se acessada
    @OneToMany(mappedBy = "communityUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    // mesma logica dos posts de um usuario
    @OneToMany(mappedBy = "communityUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now(ZoneOffset.UTC);
        }
    }


    // metodos para garantir consistencia dos relacionamentos bidirecionais
    public void addPost(Post post) {
        this.posts.add(post);
        post.setCommunityUser(this);
    }

    public void removePost(Post post) {
        this.posts.remove(post);
        post.setCommunityUser(null);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setCommunityUser(this);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setCommunityUser(null);
    }

}
