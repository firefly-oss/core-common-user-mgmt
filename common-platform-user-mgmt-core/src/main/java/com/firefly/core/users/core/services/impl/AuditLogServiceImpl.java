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


package com.firefly.core.users.core.services.impl;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.users.core.mappers.AuditLogMapper;
import com.firefly.core.users.core.services.AuditLogService;
import com.firefly.core.users.interfaces.dtos.AuditLogDTO;
import com.firefly.core.users.models.entities.AuditLog;
import com.firefly.core.users.models.repositories.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private AuditLogRepository repository;

    @Autowired
    private AuditLogMapper mapper;

    @Override
    public Mono<PaginationResponse<AuditLogDTO>> filterAuditLogs(FilterRequest<AuditLogDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        AuditLog.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<AuditLogDTO> createAuditLog(AuditLogDTO auditLogDTO) {
        return Mono.just(auditLogDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AuditLogDTO> updateAuditLog(UUID auditLogId, AuditLogDTO auditLogDTO) {
        return repository.findById(auditLogId)
                .switchIfEmpty(Mono.error(new RuntimeException("Audit log not found with ID: " + auditLogId)))
                .flatMap(existingAuditLog -> {
                    AuditLog updatedAuditLog = mapper.toEntity(auditLogDTO);
                    updatedAuditLog.setId(auditLogId);
                    return repository.save(updatedAuditLog);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteAuditLog(UUID auditLogId) {
        return repository.findById(auditLogId)
                .switchIfEmpty(Mono.error(new RuntimeException("Audit log not found with ID: " + auditLogId)))
                .flatMap(auditLog -> repository.deleteById(auditLogId));
    }

    @Override
    public Mono<AuditLogDTO> getAuditLogById(UUID auditLogId) {
        return repository.findById(auditLogId)
                .switchIfEmpty(Mono.error(new RuntimeException("Audit log not found with ID: " + auditLogId)))
                .map(mapper::toDTO);
    }
}