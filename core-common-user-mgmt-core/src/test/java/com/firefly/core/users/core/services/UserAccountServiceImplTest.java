/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.core.users.core.services;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.users.core.mappers.UserAccountMapper;
import com.firefly.core.users.core.services.impl.UserAccountServiceImpl;
import com.firefly.core.users.interfaces.dtos.UserAccountDTO;
import com.firefly.core.users.models.entities.UserAccount;
import com.firefly.core.users.models.repositories.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceImplTest {

    private static final UUID TEST_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Mock
    private UserAccountRepository repository;

    @Mock
    private UserAccountMapper mapper;

    @InjectMocks
    private UserAccountServiceImpl service;

    private UserAccount userAccount;
    private UserAccountDTO userAccountDTO;
    private FilterRequest<UserAccountDTO> filterRequest;
    private PaginationResponse<UserAccountDTO> paginationResponse;

    @BeforeEach
    void setUp() {
        // Initialize test data
        userAccount = new UserAccount();
        userAccount.setId(TEST_UUID);

        userAccountDTO = new UserAccountDTO();
        userAccountDTO.setId(TEST_UUID);

        filterRequest = new FilterRequest<>();

        // We'll mock PaginationResponse since it's from an external library
        paginationResponse = mock(PaginationResponse.class);
    }

    // We're not testing the filterUserAccounts method because it relies on FilterUtils,
    // which is an external library that we don't have access to.
    // The method simply delegates to FilterUtils.createFilter().filter(),
    // so there's not much value in testing it.

    @Test
    void createUserAccount_ShouldCreateAndReturnUserAccount() {
        // Arrange
        when(mapper.toEntity(any(UserAccountDTO.class))).thenReturn(userAccount);
        when(repository.save(any(UserAccount.class))).thenReturn(Mono.just(userAccount));
        when(mapper.toDTO(any(UserAccount.class))).thenReturn(userAccountDTO);

        // Act & Assert
        StepVerifier.create(service.createUserAccount(userAccountDTO))
                .expectNext(userAccountDTO)
                .verifyComplete();

        verify(mapper).toEntity(userAccountDTO);
        verify(repository).save(userAccount);
        verify(mapper).toDTO(userAccount);
    }

    @Test
    void updateUserAccount_WhenUserAccountExists_ShouldUpdateAndReturnUserAccount() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(userAccount));
        when(mapper.toEntity(any(UserAccountDTO.class))).thenReturn(userAccount);
        when(repository.save(any(UserAccount.class))).thenReturn(Mono.just(userAccount));
        when(mapper.toDTO(any(UserAccount.class))).thenReturn(userAccountDTO);

        // Act & Assert
        StepVerifier.create(service.updateUserAccount(TEST_UUID, userAccountDTO))
                .expectNext(userAccountDTO)
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(mapper).toEntity(userAccountDTO);
        verify(repository).save(userAccount);
        verify(mapper).toDTO(userAccount);
    }

    @Test
    void updateUserAccount_WhenUserAccountDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateUserAccount(TEST_UUID, userAccountDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("User account not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(mapper, never()).toEntity(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void deleteUserAccount_WhenUserAccountExists_ShouldDeleteUserAccount() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(userAccount));
        when(repository.deleteById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteUserAccount(TEST_UUID))
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(repository).deleteById(TEST_UUID);
    }

    @Test
    void deleteUserAccount_WhenUserAccountDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteUserAccount(TEST_UUID))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("User account not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(repository, never()).deleteById(any(UUID.class));
    }

    @Test
    void getUserAccountById_WhenUserAccountExists_ShouldReturnUserAccount() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(userAccount));
        when(mapper.toDTO(any(UserAccount.class))).thenReturn(userAccountDTO);

        // Act & Assert
        StepVerifier.create(service.getUserAccountById(TEST_UUID))
                .expectNext(userAccountDTO)
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(mapper).toDTO(userAccount);
    }

    @Test
    void getUserAccountById_WhenUserAccountDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getUserAccountById(TEST_UUID))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("User account not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(mapper, never()).toDTO(any());
    }
}
