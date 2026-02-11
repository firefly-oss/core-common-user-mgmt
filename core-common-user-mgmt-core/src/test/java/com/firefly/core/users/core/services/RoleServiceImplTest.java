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
import com.firefly.core.users.core.mappers.RoleMapper;
import com.firefly.core.users.core.services.impl.RoleServiceImpl;
import com.firefly.core.users.interfaces.dtos.RoleDTO;
import com.firefly.core.users.models.entities.Role;
import com.firefly.core.users.models.repositories.RoleRepository;
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
class RoleServiceImplTest {

    private static final UUID TEST_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Mock
    private RoleRepository repository;

    @Mock
    private RoleMapper mapper;

    @InjectMocks
    private RoleServiceImpl service;

    private Role role;
    private RoleDTO roleDTO;
    private FilterRequest<RoleDTO> filterRequest;
    private PaginationResponse<RoleDTO> paginationResponse;

    @BeforeEach
    void setUp() {
        // Initialize test data
        role = new Role();
        role.setId(TEST_UUID);

        roleDTO = new RoleDTO();
        roleDTO.setId(TEST_UUID);

        filterRequest = new FilterRequest<>();

        // We'll mock PaginationResponse since it's from an external library
        paginationResponse = mock(PaginationResponse.class);
    }

    // We're not testing the filterRoles method because it relies on FilterUtils,
    // which is an external library that we don't have access to.
    // The method simply delegates to FilterUtils.createFilter().filter(),
    // so there's not much value in testing it.

    @Test
    void createRole_ShouldCreateAndReturnRole() {
        // Arrange
        when(mapper.toEntity(any(RoleDTO.class))).thenReturn(role);
        when(repository.save(any(Role.class))).thenReturn(Mono.just(role));
        when(mapper.toDTO(any(Role.class))).thenReturn(roleDTO);

        // Act & Assert
        StepVerifier.create(service.createRole(roleDTO))
                .expectNext(roleDTO)
                .verifyComplete();

        verify(mapper).toEntity(roleDTO);
        verify(repository).save(role);
        verify(mapper).toDTO(role);
    }

    @Test
    void updateRole_WhenRoleExists_ShouldUpdateAndReturnRole() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(role));
        when(mapper.toEntity(any(RoleDTO.class))).thenReturn(role);
        when(repository.save(any(Role.class))).thenReturn(Mono.just(role));
        when(mapper.toDTO(any(Role.class))).thenReturn(roleDTO);

        // Act & Assert
        StepVerifier.create(service.updateRole(TEST_UUID, roleDTO))
                .expectNext(roleDTO)
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(mapper).toEntity(roleDTO);
        verify(repository).save(role);
        verify(mapper).toDTO(role);
    }

    @Test
    void updateRole_WhenRoleDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateRole(TEST_UUID, roleDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Role not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(mapper, never()).toEntity(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void deleteRole_WhenRoleExists_ShouldDeleteRole() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(role));
        when(repository.deleteById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteRole(TEST_UUID))
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(repository).deleteById(TEST_UUID);
    }

    @Test
    void deleteRole_WhenRoleDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteRole(TEST_UUID))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Role not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(repository, never()).deleteById(any(UUID.class));
    }

    @Test
    void getRoleById_WhenRoleExists_ShouldReturnRole() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(role));
        when(mapper.toDTO(any(Role.class))).thenReturn(roleDTO);

        // Act & Assert
        StepVerifier.create(service.getRoleById(TEST_UUID))
                .expectNext(roleDTO)
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(mapper).toDTO(role);
    }

    @Test
    void getRoleById_WhenRoleDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getRoleById(TEST_UUID))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Role not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(mapper, never()).toDTO(any());
    }
}
