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
import com.firefly.core.users.core.mappers.UserRoleMapper;
import com.firefly.core.users.core.services.impl.UserRoleServiceImpl;
import com.firefly.core.users.interfaces.dtos.UserRoleDTO;
import com.firefly.core.users.models.entities.UserRole;
import com.firefly.core.users.models.repositories.UserRoleRepository;
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
class UserRoleServiceImplTest {

    private static final UUID TEST_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Mock
    private UserRoleRepository repository;

    @Mock
    private UserRoleMapper mapper;

    @InjectMocks
    private UserRoleServiceImpl service;

    private UserRole userRole;
    private UserRoleDTO userRoleDTO;
    private FilterRequest<UserRoleDTO> filterRequest;
    private PaginationResponse<UserRoleDTO> paginationResponse;

    @BeforeEach
    void setUp() {
        // Initialize test data
        userRole = new UserRole();
        userRole.setId(TEST_UUID);

        userRoleDTO = new UserRoleDTO();
        userRoleDTO.setId(TEST_UUID);

        filterRequest = new FilterRequest<>();

        // We'll mock PaginationResponse since it's from an external library
        paginationResponse = mock(PaginationResponse.class);
    }

    // We're not testing the filterUserRoles method because it relies on FilterUtils,
    // which is an external library that we don't have access to.
    // The method simply delegates to FilterUtils.createFilter().filter(),
    // so there's not much value in testing it.

    @Test
    void createUserRole_ShouldCreateAndReturnUserRole() {
        // Arrange
        when(mapper.toEntity(any(UserRoleDTO.class))).thenReturn(userRole);
        when(repository.save(any(UserRole.class))).thenReturn(Mono.just(userRole));
        when(mapper.toDTO(any(UserRole.class))).thenReturn(userRoleDTO);

        // Act & Assert
        StepVerifier.create(service.createUserRole(userRoleDTO))
                .expectNext(userRoleDTO)
                .verifyComplete();

        verify(mapper).toEntity(userRoleDTO);
        verify(repository).save(userRole);
        verify(mapper).toDTO(userRole);
    }

    @Test
    void updateUserRole_WhenUserRoleExists_ShouldUpdateAndReturnUserRole() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(userRole));
        when(mapper.toEntity(any(UserRoleDTO.class))).thenReturn(userRole);
        when(repository.save(any(UserRole.class))).thenReturn(Mono.just(userRole));
        when(mapper.toDTO(any(UserRole.class))).thenReturn(userRoleDTO);

        // Act & Assert
        StepVerifier.create(service.updateUserRole(TEST_UUID, userRoleDTO))
                .expectNext(userRoleDTO)
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(mapper).toEntity(userRoleDTO);
        verify(repository).save(userRole);
        verify(mapper).toDTO(userRole);
    }

    @Test
    void updateUserRole_WhenUserRoleDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateUserRole(TEST_UUID, userRoleDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("User role not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(mapper, never()).toEntity(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void deleteUserRole_WhenUserRoleExists_ShouldDeleteUserRole() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(userRole));
        when(repository.deleteById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteUserRole(TEST_UUID))
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(repository).deleteById(TEST_UUID);
    }

    @Test
    void deleteUserRole_WhenUserRoleDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteUserRole(TEST_UUID))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("User role not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(repository, never()).deleteById(any(UUID.class));
    }

    @Test
    void getUserRoleById_WhenUserRoleExists_ShouldReturnUserRole() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(userRole));
        when(mapper.toDTO(any(UserRole.class))).thenReturn(userRoleDTO);

        // Act & Assert
        StepVerifier.create(service.getUserRoleById(TEST_UUID))
                .expectNext(userRoleDTO)
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(mapper).toDTO(userRole);
    }

    @Test
    void getUserRoleById_WhenUserRoleDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getUserRoleById(TEST_UUID))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("User role not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(mapper, never()).toDTO(any());
    }
}
