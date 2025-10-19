package com.fyora.community.auth;

import com.fyora.community.auth.dto.LoginRequest;
import com.fyora.community.auth.dto.RegisterRequest;
import com.fyora.community.infra.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest validRegisterRequest;
    private LoginRequest validLoginRequest;
    private UserAccount existingUserAccount;
    private String encodedPassword;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        validRegisterRequest = new RegisterRequest("testuser", "password123");
        validLoginRequest = new LoginRequest("testuser", "password123");
        encodedPassword = "$2a$10$encodedPasswordHash";
        jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
        
        existingUserAccount = new UserAccount();
        existingUserAccount.setId(1L);
        existingUserAccount.setUsername("testuser");
        existingUserAccount.setPasswordHash(encodedPassword);
        existingUserAccount.setRole("USER");
    }

    @Test
    void register_WithValidData_ShouldCreateUserAccount() {
        // Arrange
        when(userAccountRepository.existsByUsername(validRegisterRequest.username())).thenReturn(false);
        when(passwordEncoder.encode(validRegisterRequest.password())).thenReturn(encodedPassword);
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(existingUserAccount);

        // Act
        assertDoesNotThrow(() -> authService.register(validRegisterRequest));

        // Assert
        verify(userAccountRepository).existsByUsername(validRegisterRequest.username());
        verify(passwordEncoder).encode(validRegisterRequest.password());
        verify(userAccountRepository).save(any(UserAccount.class));
    }

    @Test
    void register_WithExistingUsername_ShouldThrowException() {
        // Arrange
        when(userAccountRepository.existsByUsername(validRegisterRequest.username())).thenReturn(true);

        // Act & Assert
        com.fyora.community.auth.exception.UsernameAlreadyExistsException exception = 
            assertThrows(com.fyora.community.auth.exception.UsernameAlreadyExistsException.class, 
            () -> authService.register(validRegisterRequest));
        
        assertEquals("Username already exists: " + validRegisterRequest.username(), exception.getMessage());
        verify(userAccountRepository).existsByUsername(validRegisterRequest.username());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userAccountRepository, never()).save(any(UserAccount.class));
    }

    @Test
    void login_WithValidCredentials_ShouldReturnJwtToken() {
        // Arrange
        when(userAccountRepository.findByUsername(validLoginRequest.username()))
            .thenReturn(Optional.of(existingUserAccount));
        when(passwordEncoder.matches(validLoginRequest.password(), existingUserAccount.getPasswordHash()))
            .thenReturn(true);
        when(jwtTokenProvider.generate(existingUserAccount.getUsername(), existingUserAccount.getRole()))
            .thenReturn(jwtToken);

        // Act
        String result = authService.login(validLoginRequest);

        // Assert
        assertEquals(jwtToken, result);
        verify(userAccountRepository).findByUsername(validLoginRequest.username());
        verify(passwordEncoder).matches(validLoginRequest.password(), existingUserAccount.getPasswordHash());
        verify(jwtTokenProvider).generate(existingUserAccount.getUsername(), existingUserAccount.getRole());
    }

    @Test
    void login_WithNonExistentUsername_ShouldThrowException() {
        // Arrange
        when(userAccountRepository.findByUsername(validLoginRequest.username()))
            .thenReturn(Optional.empty());

        // Act & Assert
        com.fyora.community.auth.exception.InvalidCredentialsException exception = 
            assertThrows(com.fyora.community.auth.exception.InvalidCredentialsException.class, 
            () -> authService.login(validLoginRequest));
        
        assertEquals("Invalid username or password", exception.getMessage());
        verify(userAccountRepository).findByUsername(validLoginRequest.username());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtTokenProvider, never()).generate(anyString(), anyString());
    }

    @Test
    void login_WithInvalidPassword_ShouldThrowException() {
        // Arrange
        when(userAccountRepository.findByUsername(validLoginRequest.username()))
            .thenReturn(Optional.of(existingUserAccount));
        when(passwordEncoder.matches(validLoginRequest.password(), existingUserAccount.getPasswordHash()))
            .thenReturn(false);

        // Act & Assert
        com.fyora.community.auth.exception.InvalidCredentialsException exception = 
            assertThrows(com.fyora.community.auth.exception.InvalidCredentialsException.class, 
            () -> authService.login(validLoginRequest));
        
        assertEquals("Invalid username or password", exception.getMessage());
        verify(userAccountRepository).findByUsername(validLoginRequest.username());
        verify(passwordEncoder).matches(validLoginRequest.password(), existingUserAccount.getPasswordHash());
        verify(jwtTokenProvider, never()).generate(anyString(), anyString());
    }

    @Test
    void register_ShouldEncodePasswordCorrectly() {
        // Arrange
        String rawPassword = "password123";
        String expectedEncodedPassword = "$2a$10$encodedPasswordHash";
        RegisterRequest request = new RegisterRequest("testuser", rawPassword);
        
        when(userAccountRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(expectedEncodedPassword);
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(existingUserAccount);

        // Act
        authService.register(request);

        // Assert
        verify(passwordEncoder).encode(rawPassword);
        verify(userAccountRepository).save(argThat(userAccount -> 
            userAccount.getPasswordHash().equals(expectedEncodedPassword)
        ));
    }
}
