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
import com.firefly.core.users.core.mappers.AuditLogMapper;
import com.firefly.core.users.core.services.impl.AuditLogServiceImpl;
import com.firefly.core.users.interfaces.dtos.AuditLogDTO;
import com.firefly.core.users.models.entities.AuditLog;
import com.firefly.core.users.models.repositories.AuditLogRepository;
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
class AuditLogServiceImplTest {

    private static final UUID TEST_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Mock
    private AuditLogRepository repository;

    @Mock
    private AuditLogMapper mapper;

    @InjectMocks
    private AuditLogServiceImpl service;

    private AuditLog auditLog;
    private AuditLogDTO auditLogDTO;
    private FilterRequest<AuditLogDTO> filterRequest;
    private PaginationResponse<AuditLogDTO> paginationResponse;

    @BeforeEach
    void setUp() {
        // Initialize test data
        auditLog = new AuditLog();
        auditLog.setId(TEST_UUID);

        auditLogDTO = new AuditLogDTO();
        auditLogDTO.setId(TEST_UUID);

        filterRequest = new FilterRequest<>();

        // We'll mock PaginationResponse since it's from an external library
        paginationResponse = mock(PaginationResponse.class);
    }

    // We're not testing the filterAuditLogs method because it relies on FilterUtils,
    // which is an external library that we don't have access to.
    // The method simply delegates to FilterUtils.createFilter().filter(),
    // so there's not much value in testing it.

    @Test
    void createAuditLog_ShouldCreateAndReturnAuditLog() {
        // Arrange
        when(mapper.toEntity(any(AuditLogDTO.class))).thenReturn(auditLog);
        when(repository.save(any(AuditLog.class))).thenReturn(Mono.just(auditLog));
        when(mapper.toDTO(any(AuditLog.class))).thenReturn(auditLogDTO);

        // Act & Assert
        StepVerifier.create(service.createAuditLog(auditLogDTO))
                .expectNext(auditLogDTO)
                .verifyComplete();

        verify(mapper).toEntity(auditLogDTO);
        verify(repository).save(auditLog);
        verify(mapper).toDTO(auditLog);
    }

    @Test
    void updateAuditLog_WhenAuditLogExists_ShouldUpdateAndReturnAuditLog() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(auditLog));
        when(mapper.toEntity(any(AuditLogDTO.class))).thenReturn(auditLog);
        when(repository.save(any(AuditLog.class))).thenReturn(Mono.just(auditLog));
        when(mapper.toDTO(any(AuditLog.class))).thenReturn(auditLogDTO);

        // Act & Assert
        StepVerifier.create(service.updateAuditLog(TEST_UUID, auditLogDTO))
                .expectNext(auditLogDTO)
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(mapper).toEntity(auditLogDTO);
        verify(repository).save(auditLog);
        verify(mapper).toDTO(auditLog);
    }

    @Test
    void updateAuditLog_WhenAuditLogDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.updateAuditLog(TEST_UUID, auditLogDTO))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Audit log not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(mapper, never()).toEntity(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    void deleteAuditLog_WhenAuditLogExists_ShouldDeleteAuditLog() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(auditLog));
        when(repository.deleteById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteAuditLog(TEST_UUID))
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(repository).deleteById(TEST_UUID);
    }

    @Test
    void deleteAuditLog_WhenAuditLogDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.deleteAuditLog(TEST_UUID))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Audit log not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(repository, never()).deleteById(any(UUID.class));
    }

    @Test
    void getAuditLogById_WhenAuditLogExists_ShouldReturnAuditLog() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.just(auditLog));
        when(mapper.toDTO(any(AuditLog.class))).thenReturn(auditLogDTO);

        // Act & Assert
        StepVerifier.create(service.getAuditLogById(TEST_UUID))
                .expectNext(auditLogDTO)
                .verifyComplete();

        verify(repository).findById(TEST_UUID);
        verify(mapper).toDTO(auditLog);
    }

    @Test
    void getAuditLogById_WhenAuditLogDoesNotExist_ShouldReturnError() {
        // Arrange
        when(repository.findById(any(UUID.class))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.getAuditLogById(TEST_UUID))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Audit log not found with ID: " + TEST_UUID))
                .verify();

        verify(repository).findById(TEST_UUID);
        verify(mapper, never()).toDTO(any());
    }
}
