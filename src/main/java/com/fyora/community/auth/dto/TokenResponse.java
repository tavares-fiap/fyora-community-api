package com.fyora.community.auth.dto;

public record TokenResponse(String accessToken, String tokenType) { public TokenResponse(String t){ this(t,"Bearer"); } }
