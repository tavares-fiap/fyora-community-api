package com.fyora.community.infra.security;

import com.fyora.community.auth.UserAccount;
import com.fyora.community.auth.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class CurrentUserService {
    private final UserAccountRepository users;

    public UserAccount requireUserAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user");
        }
        String username = auth.getName();
        return users.findByUsername(username)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Authenticated username not found: " + username));
    }
}
