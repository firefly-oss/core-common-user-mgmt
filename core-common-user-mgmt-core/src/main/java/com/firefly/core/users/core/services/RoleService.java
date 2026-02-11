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


package com.firefly.core.users.core.services;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.users.interfaces.dtos.RoleDTO;

import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Service interface for managing roles.
 */
public interface RoleService {
    /**
     * Filters the roles based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for RoleDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of roles
     */
    Mono<PaginationResponse<RoleDTO>> filterRoles(FilterRequest<RoleDTO> filterRequest);
    
    /**
     * Creates a new role based on the provided information.
     *
     * @param roleDTO the DTO object containing details of the role to be created
     * @return a Mono that emits the created RoleDTO object
     */
    Mono<RoleDTO> createRole(RoleDTO roleDTO);
    
    /**
     * Updates an existing role with updated information.
     *
     * @param roleId the unique identifier of the role to be updated
     * @param roleDTO the data transfer object containing the updated details of the role
     * @return a reactive Mono containing the updated RoleDTO
     */
    Mono<RoleDTO> updateRole(UUID roleId, RoleDTO roleDTO);

    /**
     * Deletes a role identified by its unique ID.
     *
     * @param roleId the unique identifier of the role to be deleted
     * @return a Mono that completes when the role is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteRole(UUID roleId);

    /**
     * Retrieves a role by its unique identifier.
     *
     * @param roleId the unique identifier of the role to retrieve
     * @return a Mono emitting the {@link RoleDTO} representing the role if found,
     *         or an empty Mono if the role does not exist
     */
    Mono<RoleDTO> getRoleById(UUID roleId);
}