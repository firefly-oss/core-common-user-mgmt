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
import com.firefly.core.users.core.mappers.UserAccountMapper;
import com.firefly.core.users.core.services.UserAccountService;
import com.firefly.core.users.interfaces.dtos.UserAccountDTO;
import com.firefly.core.users.models.entities.UserAccount;
import com.firefly.core.users.models.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserAccountRepository repository;

    @Autowired
    private UserAccountMapper mapper;

    @Override
    public Mono<PaginationResponse<UserAccountDTO>> filterUserAccounts(FilterRequest<UserAccountDTO> filterRequest) {
        return FilterUtils
                .createFilter(
                        UserAccount.class,
                        mapper::toDTO
                )
                .filter(filterRequest);
    }

    @Override
    public Mono<UserAccountDTO> createUserAccount(UserAccountDTO userAccountDTO) {
        return Mono.just(userAccountDTO)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<UserAccountDTO> updateUserAccount(UUID userAccountId, UserAccountDTO userAccountDTO) {
        return repository.findById(userAccountId)
                .switchIfEmpty(Mono.error(new RuntimeException("User account not found with ID: " + userAccountId)))
                .flatMap(existingUserAccount -> {
                    UserAccount updatedUserAccount = mapper.toEntity(userAccountDTO);
                    updatedUserAccount.setId(userAccountId);
                    return repository.save(updatedUserAccount);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteUserAccount(UUID userAccountId) {
        return repository.findById(userAccountId)
                .switchIfEmpty(Mono.error(new RuntimeException("User account not found with ID: " + userAccountId)))
                .flatMap(userAccount -> repository.deleteById(userAccountId));
    }

    @Override
    public Mono<UserAccountDTO> getUserAccountById(UUID userAccountId) {
        return repository.findById(userAccountId)
                .switchIfEmpty(Mono.error(new RuntimeException("User account not found with ID: " + userAccountId)))
                .map(mapper::toDTO);
    }
}