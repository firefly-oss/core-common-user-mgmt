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


package com.firefly.core.users.core.services;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.users.interfaces.dtos.RolePermissionDTO;

import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Service interface for managing role-permission mappings.
 */
public interface RolePermissionService {
    /**
     * Filters the role-permission mappings based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for RolePermissionDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of role-permission mappings
     */
    Mono<PaginationResponse<RolePermissionDTO>> filterRolePermissions(FilterRequest<RolePermissionDTO> filterRequest);
    
    /**
     * Creates a new role-permission mapping based on the provided information.
     *
     * @param rolePermissionDTO the DTO object containing details of the role-permission mapping to be created
     * @return a Mono that emits the created RolePermissionDTO object
     */
    Mono<RolePermissionDTO> createRolePermission(RolePermissionDTO rolePermissionDTO);
    
    /**
     * Updates an existing role-permission mapping with updated information.
     *
     * @param rolePermissionId the unique identifier of the role-permission mapping to be updated
     * @param rolePermissionDTO the data transfer object containing the updated details of the role-permission mapping
     * @return a reactive Mono containing the updated RolePermissionDTO
     */
    Mono<RolePermissionDTO> updateRolePermission(UUID rolePermissionId, RolePermissionDTO rolePermissionDTO);

    /**
     * Deletes a role-permission mapping identified by its unique ID.
     *
     * @param rolePermissionId the unique identifier of the role-permission mapping to be deleted
     * @return a Mono that completes when the role-permission mapping is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteRolePermission(UUID rolePermissionId);

    /**
     * Retrieves a role-permission mapping by its unique identifier.
     *
     * @param rolePermissionId the unique identifier of the role-permission mapping to retrieve
     * @return a Mono emitting the {@link RolePermissionDTO} representing the role-permission mapping if found,
     *         or an empty Mono if the role-permission mapping does not exist
     */
    Mono<RolePermissionDTO> getRolePermissionById(UUID rolePermissionId);
}