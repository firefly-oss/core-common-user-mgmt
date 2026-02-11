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


package com.firefly.core.users.models.repositories;

import com.firefly.core.users.interfaces.enums.ScopeTypeEnum;
import com.firefly.core.users.models.entities.Role;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository interface for Role entity.
 * Extends BaseRepository to inherit common CRUD operations.
 */
@Repository
public interface RoleRepository extends BaseRepository<Role, UUID> {
    
    /**
     * Find a role by its name.
     *
     * @param name the role name
     * @return a Mono of Role entity
     */
    Mono<Role> findByName(String name);
    
    /**
     * Find roles by assignable status.
     *
     * @param isAssignable the assignable status
     * @return a Flux of Role entities
     */
    Flux<Role> findByIsAssignable(Boolean isAssignable);
    
    /**
     * Find roles by scope type.
     *
     * @param scopeType the scope type
     * @return a Flux of Role entities
     */
    Flux<Role> findByScopeType(ScopeTypeEnum scopeType);
    
    /**
     * Check if a role with the given name exists.
     *
     * @param name the role name
     * @return a Mono of Boolean
     */
    Mono<Boolean> existsByName(String name);
}