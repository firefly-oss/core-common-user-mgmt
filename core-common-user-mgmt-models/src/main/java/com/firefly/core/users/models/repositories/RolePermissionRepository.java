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

import com.firefly.core.users.models.entities.RolePermission;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository interface for RolePermission entity.
 * Extends BaseRepository to inherit common CRUD operations.
 */
@Repository
public interface RolePermissionRepository extends BaseRepository<RolePermission, UUID> {

    /**
     * Find role permissions by role ID.
     *
     * @param roleId the role ID
     * @return a Flux of RolePermission entities
     */
    Flux<RolePermission> findByRoleId(UUID roleId);

    /**
     * Find role permissions by permission ID.
     *
     * @param permissionId the permission ID
     * @return a Flux of RolePermission entities
     */
    Flux<RolePermission> findByPermissionId(UUID permissionId);

    /**
     * Find a role permission by role ID and permission ID.
     *
     * @param roleId the role ID
     * @param permissionId the permission ID
     * @return a Mono of RolePermission entity
     */
    Mono<RolePermission> findByRoleIdAndPermissionId(UUID roleId, UUID permissionId);

    /**
     * Delete role permissions by role ID.
     *
     * @param roleId the role ID
     * @return a Mono of Void
     */
    Mono<Void> deleteByRoleId(UUID roleId);

    /**
     * Delete a role permission by role ID and permission ID.
     *
     * @param roleId the role ID
     * @param permissionId the permission ID
     * @return a Mono of Void
     */
    Mono<Void> deleteByRoleIdAndPermissionId(UUID roleId, UUID permissionId);
}