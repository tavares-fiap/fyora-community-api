package com.fyora.community.auth;

import com.fyora.community.auth.dto.LoginRequest;
import com.fyora.community.auth.dto.RegisterRequest;
import com.fyora.community.auth.exception.InvalidCredentialsException;
import com.fyora.community.auth.exception.UsernameAlreadyExistsException;
import com.fyora.community.infra.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class AuthService {
    private final UserAccountRepository repo;
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenProvider tokens;

    public void register(RegisterRequest req){
        if (repo.existsByUsername(req.username())) {
            throw new UsernameAlreadyExistsException("Username already exists: " + req.username());
        }
        var acc = new UserAccount();
        acc.setUsername(req.username());
        acc.setPasswordHash(encoder.encode(req.password()));
        repo.save(acc);
    }

    public String login(LoginRequest req){
        var acc = repo.findByUsername(req.username())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));
        if (!encoder.matches(req.password(), acc.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        return tokens.generate(acc.getUsername(), acc.getRole());
    }
}
