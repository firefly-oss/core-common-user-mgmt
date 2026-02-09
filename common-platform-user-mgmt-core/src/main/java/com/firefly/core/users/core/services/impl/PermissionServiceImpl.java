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
import com.firefly.core.users.core.mappers.PermissionMapper;
import com.firefly.core.users.core.services.PermissionService;
import com.firefly.core.users.interfaces.dtos.PermissionDTO;
import com.firefly.core.users.models.entities.Permission;
import com.firefly.core.users.models.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository repository;

    @Autowired
    private PermissionMapper mapper;

    @Override
    public Mono<PaginationResponse<PermissionDTO>> filterPermissions(FilterRequest<PermissionDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        Permission.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<PermissionDTO> createPermission(PermissionDTO permissionDTO) {
        return Mono.just(permissionDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<PermissionDTO> updatePermission(UUID permissionId, PermissionDTO permissionDTO) {
        return repository.findById(permissionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Permission not found with ID: " + permissionId)))
                .flatMap(existingPermission -> {
                    Permission updatedPermission = mapper.toEntity(permissionDTO);
                    updatedPermission.setId(permissionId);
                    return repository.save(updatedPermission);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deletePermission(UUID permissionId) {
        return repository.findById(permissionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Permission not found with ID: " + permissionId)))
                .flatMap(permission -> repository.deleteById(permissionId));
    }

    @Override
    public Mono<PermissionDTO> getPermissionById(UUID permissionId) {
        return repository.findById(permissionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Permission not found with ID: " + permissionId)))
                .map(mapper::toDTO);
    }
}