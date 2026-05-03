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


package com.firefly.core.users.core.services.impl;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.users.core.mappers.RolePermissionMapper;
import com.firefly.core.users.core.services.RolePermissionService;
import com.firefly.core.users.interfaces.dtos.RolePermissionDTO;
import com.firefly.core.users.models.entities.RolePermission;
import com.firefly.core.users.models.repositories.RolePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionRepository repository;

    @Autowired
    private RolePermissionMapper mapper;

    @Override
    public Mono<PaginationResponse<RolePermissionDTO>> filterRolePermissions(FilterRequest<RolePermissionDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        RolePermission.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<RolePermissionDTO> createRolePermission(RolePermissionDTO rolePermissionDTO) {
        return Mono.just(rolePermissionDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<RolePermissionDTO> updateRolePermission(UUID rolePermissionId, RolePermissionDTO rolePermissionDTO) {
        return repository.findById(rolePermissionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Role-Permission mapping not found with ID: " + rolePermissionId)))
                .flatMap(existingRolePermission -> {
                    RolePermission updatedRolePermission = mapper.toEntity(rolePermissionDTO);
                    updatedRolePermission.setId(rolePermissionId);
                    return repository.save(updatedRolePermission);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteRolePermission(UUID rolePermissionId) {
        return repository.findById(rolePermissionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Role-Permission mapping not found with ID: " + rolePermissionId)))
                .flatMap(rolePermission -> repository.deleteById(rolePermissionId));
    }

    @Override
    public Mono<RolePermissionDTO> getRolePermissionById(UUID rolePermissionId) {
        return repository.findById(rolePermissionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Role-Permission mapping not found with ID: " + rolePermissionId)))
                .map(mapper::toDTO);
    }
}