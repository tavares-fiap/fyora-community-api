package com.fyora.community.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Algorithm algorithm;
    private final String issuer;
    private final long expirationSeconds;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.expiration-seconds}") long expirationSeconds
    ) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.issuer = issuer;
        this.expirationSeconds = expirationSeconds;
    }

    public String generate(String username, String role) {
        Instant now = Instant.now();
        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(expirationSeconds)))
                .withSubject(username)
                .withClaim("role", role)
                .sign(algorithm);
    }

    public DecodedJWT validate(String token) {
        return JWT.require(algorithm).withIssuer(issuer).build().verify(token);
    }
}
