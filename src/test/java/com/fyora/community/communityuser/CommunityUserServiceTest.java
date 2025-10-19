package com.fyora.community.communityuser;

import com.fyora.community.auth.UserAccount;
import com.fyora.community.common.NomeComunitarioGenerator;
import com.fyora.community.common.exception.ResourceNotFoundException;
import com.fyora.community.communityuser.dto.DadosListagemCommunityUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityUserServiceTest {

    @Mock
    private CommunityUserRepository repository;

    @Mock
    private NomeComunitarioGenerator generator;

    @InjectMocks
    private CommunityUserService communityUserService;

    private UserAccount userAccount;
    private CommunityUser communityUser;
    private String generatedName;

    @BeforeEach
    void setUp() {
        userAccount = new UserAccount();
        userAccount.setId(1L);
        userAccount.setUsername("testuser");
        userAccount.setRole("USER");

        communityUser = new CommunityUser();
        communityUser.setId(1L);
        communityUser.setCommunityName("Fênix Valente #1234");
        communityUser.setCreatedAt(LocalDateTime.now());
        communityUser.setUserAccount(userAccount);

        generatedName = "Fênix Valente #1234";
    }

    @Test
    void criar_ShouldCreateNewCommunityUser() {
        // Arrange
        when(generator.gerarUnico()).thenReturn(generatedName);
        when(repository.save(any(CommunityUser.class))).thenAnswer(invocation -> {
            CommunityUser cu = invocation.getArgument(0);
            cu.setId(1L);
            cu.setCreatedAt(LocalDateTime.now());
            return cu;
        });

        // Act
        DadosListagemCommunityUser result = communityUserService.criar();

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(generatedName, result.communityName());
        assertNotNull(result.createdAt());

        verify(generator).gerarUnico();
        verify(repository).save(any(CommunityUser.class));
    }

    @Test
    void getById_WithExistingId_ShouldReturnCommunityUser() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(communityUser));

        // Act
        CommunityUser result = communityUserService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(communityUser.getId(), result.getId());
        assertEquals(communityUser.getCommunityName(), result.getCommunityName());
        assertEquals(communityUser.getUserAccount(), result.getUserAccount());

        verify(repository).findById(1L);
    }

    @Test
    void getById_WithNonExistentId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> communityUserService.getById(1L));
        
        assertEquals("CommunityUser not found", exception.getMessage());
        verify(repository).findById(1L);
    }

    @Test
    void getOrCreateByAccount_WithExistingCommunityUser_ShouldReturnExisting() {
        // Arrange
        when(repository.findByUserAccountId(userAccount.getId())).thenReturn(Optional.of(communityUser));

        // Act
        CommunityUser result = communityUserService.getOrCreateByAccount(userAccount);

        // Assert
        assertNotNull(result);
        assertEquals(communityUser.getId(), result.getId());
        assertEquals(communityUser.getCommunityName(), result.getCommunityName());
        assertEquals(communityUser.getUserAccount(), result.getUserAccount());

        verify(repository).findByUserAccountId(userAccount.getId());
        verify(generator, never()).gerarUnico();
        verify(repository, never()).save(any(CommunityUser.class));
    }

    @Test
    void getOrCreateByAccount_WithNonExistentCommunityUser_ShouldCreateNew() {
        // Arrange
        when(repository.findByUserAccountId(userAccount.getId())).thenReturn(Optional.empty());
        when(generator.gerarUnico()).thenReturn(generatedName);
        when(repository.save(any(CommunityUser.class))).thenReturn(communityUser);

        // Act
        CommunityUser result = communityUserService.getOrCreateByAccount(userAccount);

        // Assert
        assertNotNull(result);
        assertEquals(communityUser.getId(), result.getId());
        assertEquals(communityUser.getCommunityName(), result.getCommunityName());
        assertEquals(communityUser.getUserAccount(), result.getUserAccount());

        verify(repository).findByUserAccountId(userAccount.getId());
        verify(generator).gerarUnico();
        verify(repository).save(argThat(cu -> 
            cu.getUserAccount().equals(userAccount) && 
            cu.getCommunityName().equals(generatedName)
        ));
    }

    @Test
    void getOrCreateByAccount_ShouldSetCorrectUserAccount() {
        // Arrange
        when(repository.findByUserAccountId(userAccount.getId())).thenReturn(Optional.empty());
        when(generator.gerarUnico()).thenReturn(generatedName);
        when(repository.save(any(CommunityUser.class))).thenAnswer(invocation -> {
            CommunityUser cu = invocation.getArgument(0);
            cu.setId(1L);
            cu.setCreatedAt(LocalDateTime.now());
            return cu;
        });

        // Act
        CommunityUser result = communityUserService.getOrCreateByAccount(userAccount);

        // Assert
        assertNotNull(result);
        assertEquals(userAccount, result.getUserAccount());
        assertEquals(generatedName, result.getCommunityName());

        verify(repository).save(argThat(cu -> 
            cu.getUserAccount().equals(userAccount) && 
            cu.getCommunityName().equals(generatedName)
        ));
    }

    @Test
    void getOrCreateByAccount_ShouldGenerateUniqueName() {
        // Arrange
        when(repository.findByUserAccountId(userAccount.getId())).thenReturn(Optional.empty());
        when(generator.gerarUnico()).thenReturn(generatedName);
        when(repository.save(any(CommunityUser.class))).thenReturn(communityUser);

        // Act
        communityUserService.getOrCreateByAccount(userAccount);

        // Assert
        verify(generator).gerarUnico();
        verify(repository).save(argThat(cu -> 
            cu.getCommunityName().equals(generatedName)
        ));
    }
}
