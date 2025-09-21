package com.fyora.community.post;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportRepository extends JpaRepository<Support, Long> {
    boolean existsByPostIdAndCommunityUserId(Long postId, Long userId);
    long countByPostId(Long postId);
    void deleteByPostIdAndCommunityUserId(Long postId, Long userId);
}
