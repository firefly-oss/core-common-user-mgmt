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
import com.firefly.core.users.core.mappers.UserExternalIdentityMapper;
import com.firefly.core.users.core.services.impl.UserExternalIdentityServiceImpl;
import com.firefly.core.users.interfaces.dtos.UserExternalIdentityDTO;
import com.firefly.core.users.models.entities.UserExternalIdentity;
import com.firefly.core.users.models.repositories.UserExternalIdentityRepository;
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
class UserExternalIdentityServiceImplTest {

    private static final UUID TEST_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Mock
    private UserExternalIdentityRepository repository;

    @Mock
    private UserExternalIdentityMapper mapper;

    @InjectMocks
    private UserExternalIdentityServiceImpl service;

    private UserExternalIdentity userExternalIdentity;
    private UserExternalIdentityDTO userExternalIdentityDTO;
    private FilterRequest<UserExternalIdentityDTO> filterRequest;
    private PaginationResponse<UserExternalIdentityDTO> paginationResponse;

    @BeforeEach
    void setUp() {
        // Initialize test data
        userExternalIdentity = new UserExternalIdentity();
        userExternalIdentity.setId(TEST_UUID);

        userExternalIdentityDTO = new UserExternalIdentityDTO();
        userExternalIdentityDTO.setId(TEST_UUID);

        filterRequest = new FilterRequest<>();

        // We'll mock PaginationResponse since it's from an external library
        paginationResponse = mock(PaginationResponse.class);
    }

    // We're not testing the filterUserExternalIdentities method because it relies on FilterUtils,
    // which is an external library that we don't have access to.
    // The method simply delegates to FilterUtils.createFilter().filter(),
    // so there's not much value in testing it.

    @Test
    void createUserExternalIdentity_ShouldCreateAndReturnUserExternalIdentity() {
        // Arrange
        when(mapper.toEntity(any(UserExternalIdentityDTO.class))).thenReturn(userExternalIdentity);
        when(repository.save(any(UserExternalIdentity.class))).thenReturn(Mono.just(userExternalIdentity));
        when(mapper.toDTO(any(UserExternalIdentity.class))).thenReturn(userExternalIdentityDTO);

        // Act & Assert
        StepVerifier.create(service.createUserExternalIdentity(userExternalIdentityDTO))
                .expectNext(userExternalIdentityDTO)
                .verifyComplete();

        verify(mapper).toEntity(userExternalIdentityDTO);
        verify(repository).save(userExternalIdentity);
        verify(mapper).toDTO(userExternalIdentity);
    }

    @Test
    void updateUserExternalIdentity_WhenUserExternalIdentityExists_ShouldUpdateAndReturnUserExternalIdentity() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(userExternalIdentity));
        when(mapper.toEntity(any(UserExternalIdentityDTO.class))).thenReturn(userExternalIdentity);
        when(repository.save(any(UserExternalIdentity.class))).thenReturn(Mono.just(userExternalIdentity));
        when(mapper.toDTO(any(UserExternalIdentity.class))).thenReturn(userExternalIdentityDTO);

        // Act & Assert
        StepVerifier.create(service.updateUserExternalIdentity(TEST_UUID, userExternalIdentityDTO))
                .expectNext(userExternalIdentityDTO)
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(mapper).toEntity(userExternalIdentityDTO);
        verify(repository).save(userExternalIdentity);
        verify(mapper).toDTO(userExternalIdentity);
    }

    @Test
    void updateUserExternalIdentity_WhenUserExternalIdentityDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateUserExternalIdentity(TEST_UUID, userExternalIdentityDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("User external identity not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(mapper, never()).toEntity(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void deleteUserExternalIdentity_WhenUserExternalIdentityExists_ShouldDeleteUserExternalIdentity() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(userExternalIdentity));
        when(repository.deleteById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteUserExternalIdentity(TEST_UUID))
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(repository).deleteById(TEST_UUID);
    }

    @Test
    void deleteUserExternalIdentity_WhenUserExternalIdentityDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteUserExternalIdentity(TEST_UUID))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("User external identity not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(repository, never()).deleteById(any(UUID.class));
    }

    @Test
    void getUserExternalIdentityById_WhenUserExternalIdentityExists_ShouldReturnUserExternalIdentity() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(userExternalIdentity));
        when(mapper.toDTO(any(UserExternalIdentity.class))).thenReturn(userExternalIdentityDTO);

        // Act & Assert
        StepVerifier.create(service.getUserExternalIdentityById(TEST_UUID))
                .expectNext(userExternalIdentityDTO)
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(mapper).toDTO(userExternalIdentity);
    }

    @Test
    void getUserExternalIdentityById_WhenUserExternalIdentityDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getUserExternalIdentityById(TEST_UUID))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("User external identity not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(mapper, never()).toDTO(any());
    }
}
