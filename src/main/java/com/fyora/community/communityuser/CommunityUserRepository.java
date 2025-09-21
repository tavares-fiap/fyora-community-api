package com.fyora.community.communityuser;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CommunityUserRepository extends JpaRepository<CommunityUser, Long> {
    Optional<CommunityUser> findByCommunityName(String communityName);
    boolean existsByCommunityName(String communityName);
}
