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
import com.firefly.core.users.core.mappers.PermissionMapper;
import com.firefly.core.users.core.services.impl.PermissionServiceImpl;
import com.firefly.core.users.interfaces.dtos.PermissionDTO;
import com.firefly.core.users.models.entities.Permission;
import com.firefly.core.users.models.repositories.PermissionRepository;
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
class PermissionServiceImplTest {

    private static final UUID TEST_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Mock
    private PermissionRepository repository;

    @Mock
    private PermissionMapper mapper;

    @InjectMocks
    private PermissionServiceImpl service;

    private Permission permission;
    private PermissionDTO permissionDTO;
    private FilterRequest<PermissionDTO> filterRequest;
    private PaginationResponse<PermissionDTO> paginationResponse;

    @BeforeEach
    void setUp() {
        // Initialize test data
        permission = new Permission();
        permission.setId(TEST_UUID);

        permissionDTO = new PermissionDTO();
        permissionDTO.setId(TEST_UUID);

        filterRequest = new FilterRequest<>();

        // We'll mock PaginationResponse since it's from an external library
        paginationResponse = mock(PaginationResponse.class);
    }

    // We're not testing the filterPermissions method because it relies on FilterUtils,
    // which is an external library that we don't have access to.
    // The method simply delegates to FilterUtils.createFilter().filter(),
    // so there's not much value in testing it.

    @Test
    void createPermission_ShouldCreateAndReturnPermission() {
        // Arrange
        when(mapper.toEntity(any(PermissionDTO.class))).thenReturn(permission);
        when(repository.save(any(Permission.class))).thenReturn(Mono.just(permission));
        when(mapper.toDTO(any(Permission.class))).thenReturn(permissionDTO);

        // Act & Assert
        StepVerifier.create(service.createPermission(permissionDTO))
                .expectNext(permissionDTO)
                .verifyComplete();

        verify(mapper).toEntity(permissionDTO);
        verify(repository).save(permission);
        verify(mapper).toDTO(permission);
    }

    @Test
    void updatePermission_WhenPermissionExists_ShouldUpdateAndReturnPermission() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(permission));
        when(mapper.toEntity(any(PermissionDTO.class))).thenReturn(permission);
        when(repository.save(any(Permission.class))).thenReturn(Mono.just(permission));
        when(mapper.toDTO(any(Permission.class))).thenReturn(permissionDTO);

        // Act & Assert
        StepVerifier.create(service.updatePermission(TEST_UUID, permissionDTO))
                .expectNext(permissionDTO)
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(mapper).toEntity(permissionDTO);
        verify(repository).save(permission);
        verify(mapper).toDTO(permission);
    }

    @Test
    void updatePermission_WhenPermissionDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updatePermission(TEST_UUID, permissionDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Permission not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(mapper, never()).toEntity(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void deletePermission_WhenPermissionExists_ShouldDeletePermission() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(permission));
        when(repository.deleteById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deletePermission(TEST_UUID))
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(repository).deleteById(TEST_UUID);
    }

    @Test
    void deletePermission_WhenPermissionDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deletePermission(TEST_UUID))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Permission not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(repository, never()).deleteById(any(UUID.class));
    }

    @Test
    void getPermissionById_WhenPermissionExists_ShouldReturnPermission() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(permission));
        when(mapper.toDTO(any(Permission.class))).thenReturn(permissionDTO);

        // Act & Assert
        StepVerifier.create(service.getPermissionById(TEST_UUID))
                .expectNext(permissionDTO)
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(mapper).toDTO(permission);
    }

    @Test
    void getPermissionById_WhenPermissionDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getPermissionById(TEST_UUID))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Permission not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(mapper, never()).toDTO(any());
    }
}
