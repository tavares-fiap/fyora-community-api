package com.fyora.community.auth;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="user_accounts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "userAccount", fetch = FetchType.LAZY)
    private com.fyora.community.communityuser.CommunityUser communityUser;


    @Column(unique = true, nullable = false) private String username;
    @Column(name="password_hash", nullable = false) private String passwordHash;
    @Column(nullable = false) private String role = "USER";
    @Column(name="created_at", nullable = false) private LocalDateTime createdAt = LocalDateTime.now();
}
