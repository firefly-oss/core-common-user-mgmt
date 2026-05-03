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
import com.firefly.core.users.core.mappers.UserExternalIdentityMapper;
import com.firefly.core.users.core.services.UserExternalIdentityService;
import com.firefly.core.users.interfaces.dtos.UserExternalIdentityDTO;
import com.firefly.core.users.models.entities.UserExternalIdentity;
import com.firefly.core.users.models.repositories.UserExternalIdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class UserExternalIdentityServiceImpl implements UserExternalIdentityService {

    @Autowired
    private UserExternalIdentityRepository repository;

    @Autowired
    private UserExternalIdentityMapper mapper;

    @Override
    public Mono<PaginationResponse<UserExternalIdentityDTO>> filterUserExternalIdentities(FilterRequest<UserExternalIdentityDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        UserExternalIdentity.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<UserExternalIdentityDTO> createUserExternalIdentity(UserExternalIdentityDTO userExternalIdentityDTO) {
        return Mono.just(userExternalIdentityDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<UserExternalIdentityDTO> updateUserExternalIdentity(UUID userExternalIdentityId, UserExternalIdentityDTO userExternalIdentityDTO) {
        return repository.findById(userExternalIdentityId)
                .switchIfEmpty(Mono.error(new RuntimeException("User external identity not found with ID: " + userExternalIdentityId)))
                .flatMap(existingUserExternalIdentity -> {
                    UserExternalIdentity updatedUserExternalIdentity = mapper.toEntity(userExternalIdentityDTO);
                    updatedUserExternalIdentity.setId(userExternalIdentityId);
                    return repository.save(updatedUserExternalIdentity);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteUserExternalIdentity(UUID userExternalIdentityId) {
        return repository.findById(userExternalIdentityId)
                .switchIfEmpty(Mono.error(new RuntimeException("User external identity not found with ID: " + userExternalIdentityId)))
                .flatMap(userExternalIdentity -> repository.deleteById(userExternalIdentityId));
    }

    @Override
    public Mono<UserExternalIdentityDTO> getUserExternalIdentityById(UUID userExternalIdentityId) {
        return repository.findById(userExternalIdentityId)
                .switchIfEmpty(Mono.error(new RuntimeException("User external identity not found with ID: " + userExternalIdentityId)))
                .map(mapper::toDTO);
    }
}