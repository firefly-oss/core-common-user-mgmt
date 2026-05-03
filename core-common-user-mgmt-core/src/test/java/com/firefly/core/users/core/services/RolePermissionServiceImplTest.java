/*
 * Copyright 2025 Firefly Software Foundation
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
import com.firefly.core.users.core.mappers.RolePermissionMapper;
import com.firefly.core.users.core.services.impl.RolePermissionServiceImpl;
import com.firefly.core.users.interfaces.dtos.RolePermissionDTO;
import com.firefly.core.users.models.entities.RolePermission;
import com.firefly.core.users.models.repositories.RolePermissionRepository;
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
class RolePermissionServiceImplTest {

    private static final UUID TEST_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Mock
    private RolePermissionRepository repository;

    @Mock
    private RolePermissionMapper mapper;

    @InjectMocks
    private RolePermissionServiceImpl service;

    private RolePermission rolePermission;
    private RolePermissionDTO rolePermissionDTO;
    private FilterRequest<RolePermissionDTO> filterRequest;
    private PaginationResponse<RolePermissionDTO> paginationResponse;

    @BeforeEach
    void setUp() {
        // Initialize test data
        rolePermission = new RolePermission();
        rolePermission.setId(TEST_UUID);

        rolePermissionDTO = new RolePermissionDTO();
        rolePermissionDTO.setId(TEST_UUID);

        filterRequest = new FilterRequest<>();

        // We'll mock PaginationResponse since it's from an external library
        paginationResponse = mock(PaginationResponse.class);
    }

    // We're not testing the filterRolePermissions method because it relies on FilterUtils,
    // which is an external library that we don't have access to.
    // The method simply delegates to FilterUtils.createFilter().filter(),
    // so there's not much value in testing it.

    @Test
    void createRolePermission_ShouldCreateAndReturnRolePermission() {
        // Arrange
        when(mapper.toEntity(any(RolePermissionDTO.class))).thenReturn(rolePermission);
        when(repository.save(any(RolePermission.class))).thenReturn(Mono.just(rolePermission));
        when(mapper.toDTO(any(RolePermission.class))).thenReturn(rolePermissionDTO);

        // Act & Assert
        StepVerifier.create(service.createRolePermission(rolePermissionDTO))
                .expectNext(rolePermissionDTO)
                .verifyComplete();

        verify(mapper).toEntity(rolePermissionDTO);
        verify(repository).save(rolePermission);
        verify(mapper).toDTO(rolePermission);
    }

    @Test
    void updateRolePermission_WhenRolePermissionExists_ShouldUpdateAndReturnRolePermission() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(rolePermission));
        when(mapper.toEntity(any(RolePermissionDTO.class))).thenReturn(rolePermission);
        when(repository.save(any(RolePermission.class))).thenReturn(Mono.just(rolePermission));
        when(mapper.toDTO(any(RolePermission.class))).thenReturn(rolePermissionDTO);

        // Act & Assert
        StepVerifier.create(service.updateRolePermission(TEST_UUID, rolePermissionDTO))
                .expectNext(rolePermissionDTO)
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(mapper).toEntity(rolePermissionDTO);
        verify(repository).save(rolePermission);
        verify(mapper).toDTO(rolePermission);
    }

    @Test
    void updateRolePermission_WhenRolePermissionDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateRolePermission(TEST_UUID, rolePermissionDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Role-Permission mapping not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(mapper, never()).toEntity(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void deleteRolePermission_WhenRolePermissionExists_ShouldDeleteRolePermission() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(rolePermission));
        when(repository.deleteById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteRolePermission(TEST_UUID))
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(repository).deleteById(TEST_UUID);
    }

    @Test
    void deleteRolePermission_WhenRolePermissionDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteRolePermission(TEST_UUID))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Role-Permission mapping not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(repository, never()).deleteById(any(UUID.class));
    }

    @Test
    void getRolePermissionById_WhenRolePermissionExists_ShouldReturnRolePermission() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(rolePermission));
        when(mapper.toDTO(any(RolePermission.class))).thenReturn(rolePermissionDTO);

        // Act & Assert
        StepVerifier.create(service.getRolePermissionById(TEST_UUID))
                .expectNext(rolePermissionDTO)
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(mapper).toDTO(rolePermission);
    }

    @Test
    void getRolePermissionById_WhenRolePermissionDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getRolePermissionById(TEST_UUID))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Role-Permission mapping not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(mapper, never()).toDTO(any());
    }
}
