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
import com.firefly.core.users.interfaces.dtos.UserExternalIdentityDTO;

import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Service interface for managing user external identities.
 */
public interface UserExternalIdentityService {
    /**
     * Filters the user external identities based on the given criteria.
     *
     * @param filterRequest the request object containing filtering criteria for UserExternalIdentityDTO
     * @return a reactive {@code Mono} emitting a {@code PaginationResponse} containing the filtered list of user external identities
     */
    Mono<PaginationResponse<UserExternalIdentityDTO>> filterUserExternalIdentities(FilterRequest<UserExternalIdentityDTO> filterRequest);
    
    /**
     * Creates a new user external identity based on the provided information.
     *
     * @param userExternalIdentityDTO the DTO object containing details of the user external identity to be created
     * @return a Mono that emits the created UserExternalIdentityDTO object
     */
    Mono<UserExternalIdentityDTO> createUserExternalIdentity(UserExternalIdentityDTO userExternalIdentityDTO);
    
    /**
     * Updates an existing user external identity with updated information.
     *
     * @param userExternalIdentityId the unique identifier of the user external identity to be updated
     * @param userExternalIdentityDTO the data transfer object containing the updated details of the user external identity
     * @return a reactive Mono containing the updated UserExternalIdentityDTO
     */
    Mono<UserExternalIdentityDTO> updateUserExternalIdentity(UUID userExternalIdentityId, UserExternalIdentityDTO userExternalIdentityDTO);

    /**
     * Deletes a user external identity identified by its unique ID.
     *
     * @param userExternalIdentityId the unique identifier of the user external identity to be deleted
     * @return a Mono that completes when the user external identity is successfully deleted or errors if the deletion fails
     */
    Mono<Void> deleteUserExternalIdentity(UUID userExternalIdentityId);

    /**
     * Retrieves a user external identity by its unique identifier.
     *
     * @param userExternalIdentityId the unique identifier of the user external identity to retrieve
     * @return a Mono emitting the {@link UserExternalIdentityDTO} representing the user external identity if found,
     *         or an empty Mono if the user external identity does not exist
     */
    Mono<UserExternalIdentityDTO> getUserExternalIdentityById(UUID userExternalIdentityId);
}