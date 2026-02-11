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

import com.firefly.core.users.models.entities.Permission;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository interface for Permission entity.
 * Extends BaseRepository to inherit common CRUD operations.
 */
@Repository
public interface PermissionRepository extends BaseRepository<Permission, UUID> {
    
    /**
     * Find a permission by its name.
     *
     * @param name the permission name
     * @return a Mono of Permission entity
     */
    Mono<Permission> findByName(String name);
    
    /**
     * Find permissions by domain.
     *
     * @param domain the domain
     * @return a Flux of Permission entities
     */
    Flux<Permission> findByDomain(String domain);
    
    /**
     * Check if a permission with the given name exists.
     *
     * @param name the permission name
     * @return a Mono of Boolean
     */
    Mono<Boolean> existsByName(String name);
}