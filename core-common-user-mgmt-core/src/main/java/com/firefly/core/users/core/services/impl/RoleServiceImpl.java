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
import com.firefly.core.users.core.mappers.RoleMapper;
import com.firefly.core.users.core.services.RoleService;
import com.firefly.core.users.interfaces.dtos.RoleDTO;
import com.firefly.core.users.models.entities.Role;
import com.firefly.core.users.models.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository repository;

    @Autowired
    private RoleMapper mapper;

    @Override
    public Mono<PaginationResponse<RoleDTO>> filterRoles(FilterRequest<RoleDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        Role.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<RoleDTO> createRole(RoleDTO roleDTO) {
        return Mono.just(roleDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<RoleDTO> updateRole(UUID roleId, RoleDTO roleDTO) {
        return repository.findById(roleId)
                .switchIfEmpty(Mono.error(new RuntimeException("Role not found with ID: " + roleId)))
                .flatMap(existingRole -> {
                    Role updatedRole = mapper.toEntity(roleDTO);
                    updatedRole.setId(roleId);
                    return repository.save(updatedRole);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteRole(UUID roleId) {
        return repository.findById(roleId)
                .switchIfEmpty(Mono.error(new RuntimeException("Role not found with ID: " + roleId)))
                .flatMap(role -> repository.deleteById(roleId));
    }

    @Override
    public Mono<RoleDTO> getRoleById(UUID roleId) {
        return repository.findById(roleId)
                .switchIfEmpty(Mono.error(new RuntimeException("Role not found with ID: " + roleId)))
                .map(mapper::toDTO);
    }
}