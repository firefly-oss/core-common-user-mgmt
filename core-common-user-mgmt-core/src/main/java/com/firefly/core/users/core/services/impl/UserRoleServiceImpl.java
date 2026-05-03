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
import com.firefly.core.users.core.mappers.UserRoleMapper;
import com.firefly.core.users.core.services.UserRoleService;
import com.firefly.core.users.interfaces.dtos.UserRoleDTO;
import com.firefly.core.users.models.entities.UserRole;
import com.firefly.core.users.models.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleRepository repository;

    @Autowired
    private UserRoleMapper mapper;

    @Override
    public Mono<PaginationResponse<UserRoleDTO>> filterUserRoles(FilterRequest<UserRoleDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        UserRole.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<UserRoleDTO> createUserRole(UserRoleDTO userRoleDTO) {
        return Mono.just(userRoleDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<UserRoleDTO> updateUserRole(UUID userRoleId, UserRoleDTO userRoleDTO) {
        return repository.findById(userRoleId)
                .switchIfEmpty(Mono.error(new RuntimeException("User role not found with ID: " + userRoleId)))
                .flatMap(existingUserRole -> {
                    UserRole updatedUserRole = mapper.toEntity(userRoleDTO);
                    updatedUserRole.setId(userRoleId);
                    return repository.save(updatedUserRole);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteUserRole(UUID userRoleId) {
        return repository.findById(userRoleId)
                .switchIfEmpty(Mono.error(new RuntimeException("User role not found with ID: " + userRoleId)))
                .flatMap(userRole -> repository.deleteById(userRoleId));
    }

    @Override
    public Mono<UserRoleDTO> getUserRoleById(UUID userRoleId) {
        return repository.findById(userRoleId)
                .switchIfEmpty(Mono.error(new RuntimeException("User role not found with ID: " + userRoleId)))
                .map(mapper::toDTO);
    }
}