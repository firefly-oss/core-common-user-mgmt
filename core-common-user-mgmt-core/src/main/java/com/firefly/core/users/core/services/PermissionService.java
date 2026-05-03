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
import com.firefly.core.users.interfaces.dtos.PermissionDTO;

import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Service interface for managing permissions.
 */
public interface PermissionService {
    /**
     * Filters the permissions based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for PermissionDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of permissions
     */
    Mono<PaginationResponse<PermissionDTO>> filterPermissions(FilterRequest<PermissionDTO> filterRequest);
    
    /**
     * Creates a new permission based on the provided information.
     *
     * @param permissionDTO the DTO object containing details of the permission to be created
     * @return a Mono that emits the created PermissionDTO object
     */
    Mono<PermissionDTO> createPermission(PermissionDTO permissionDTO);
    
    /**
     * Updates an existing permission with updated information.
     *
     * @param permissionId the unique identifier of the permission to be updated
     * @param permissionDTO the data transfer object containing the updated details of the permission
     * @return a reactive Mono containing the updated PermissionDTO
     */
    Mono<PermissionDTO> updatePermission(UUID permissionId, PermissionDTO permissionDTO);

    /**
     * Deletes a permission identified by its unique ID.
     *
     * @param permissionId the unique identifier of the permission to be deleted
     * @return a Mono that completes when the permission is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deletePermission(UUID permissionId);

    /**
     * Retrieves a permission by its unique identifier.
     *
     * @param permissionId the unique identifier of the permission to retrieve
     * @return a Mono emitting the {@link PermissionDTO} representing the permission if found,
     *         or an empty Mono if the permission does not exist
     */
    Mono<PermissionDTO> getPermissionById(UUID permissionId);
}