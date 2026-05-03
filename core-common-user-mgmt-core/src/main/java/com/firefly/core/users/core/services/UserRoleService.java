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
import com.firefly.core.users.interfaces.dtos.UserRoleDTO;

import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Service interface for managing user roles.
 */
public interface UserRoleService {
    /**
     * Filters the user roles based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for UserRoleDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of user roles
     */
    Mono<PaginationResponse<UserRoleDTO>> filterUserRoles(FilterRequest<UserRoleDTO> filterRequest);
    
    /**
     * Creates a new user role based on the provided information.
     *
     * @param userRoleDTO the DTO object containing details of the user role to be created
     * @return a Mono that emits the created UserRoleDTO object
     */
    Mono<UserRoleDTO> createUserRole(UserRoleDTO userRoleDTO);
    
    /**
     * Updates an existing user role with updated information.
     *
     * @param userRoleId the unique identifier of the user role to be updated
     * @param userRoleDTO the data transfer object containing the updated details of the user role
     * @return a reactive Mono containing the updated UserRoleDTO
     */
    Mono<UserRoleDTO> updateUserRole(UUID userRoleId, UserRoleDTO userRoleDTO);

    /**
     * Deletes a user role identified by its unique ID.
     *
     * @param userRoleId the unique identifier of the user role to be deleted
     * @return a Mono that completes when the user role is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteUserRole(UUID userRoleId);

    /**
     * Retrieves a user role by its unique identifier.
     *
     * @param userRoleId the unique identifier of the user role to retrieve
     * @return a Mono emitting the {@link UserRoleDTO} representing the user role if found,
     *         or an empty Mono if the user role does not exist
     */
    Mono<UserRoleDTO> getUserRoleById(UUID userRoleId);
}