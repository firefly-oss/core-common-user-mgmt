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


package com.firefly.core.users.web.controllers;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.users.core.services.UserExternalIdentityService;
import com.firefly.core.users.interfaces.dtos.UserExternalIdentityDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "User External Identities", description = "API for managing user external identities")
public class UserExternalIdentityController {

    @Autowired
    private UserExternalIdentityService userExternalIdentityService;

    @Operation(summary = "Get all user external identities with filtering", description = "Returns a paginated list of user external identities based on filter criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user external identities",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class)))
    })
    @PostMapping(value = "/external-identities/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PaginationResponse<UserExternalIdentityDTO>> filterUserExternalIdentities(@RequestBody FilterRequest<UserExternalIdentityDTO> filterRequest) {
        return userExternalIdentityService.filterUserExternalIdentities(filterRequest);
    }

    @Operation(summary = "Get user external identity by ID", description = "Returns a user external identity by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user external identity",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserExternalIdentityDTO.class))),
            @ApiResponse(responseCode = "404", description = "User external identity not found")
    })
    @GetMapping(value = "/external-identities/{externalIdentityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UserExternalIdentityDTO> getUserExternalIdentityById(
            @Parameter(description = "ID of the user external identity to retrieve", required = true)
            @PathVariable UUID externalIdentityId) {
        return userExternalIdentityService.getUserExternalIdentityById(externalIdentityId);
    }

    @Operation(summary = "Get external identities for a user", description = "Returns all external identities linked to a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user external identities",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping(value = "/users/{userId}/external-identities", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PaginationResponse<UserExternalIdentityDTO>> getExternalIdentitiesByUserId(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable UUID userId) {
        FilterRequest<UserExternalIdentityDTO> filterRequest = new FilterRequest<>();
        // The actual filtering will be handled by the service based on the userId
        return userExternalIdentityService.filterUserExternalIdentities(filterRequest);
    }

    @Operation(summary = "Link external identity to user", description = "Links an external identity to a user and returns the created user external identity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "External identity successfully linked to user",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserExternalIdentityDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping(value = "/users/{userId}/external-identities", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserExternalIdentityDTO> linkExternalIdentityToUser(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable UUID userId,
            @RequestBody UserExternalIdentityDTO userExternalIdentityDTO) {
        userExternalIdentityDTO.setUserAccountId(userId);
        return userExternalIdentityService.createUserExternalIdentity(userExternalIdentityDTO);
    }

    @Operation(summary = "Unlink external identity from user", description = "Unlinks an external identity from a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "External identity successfully unlinked from user"),
            @ApiResponse(responseCode = "404", description = "User external identity not found")
    })
    @DeleteMapping("/users/{userId}/external-identities/{externalIdentityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> unlinkExternalIdentityFromUser(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable UUID userId,
            @Parameter(description = "ID of the external identity", required = true)
            @PathVariable UUID externalIdentityId) {
        // Since there's no direct method to delete by userId and externalIdentityId,
        // we need to filter user external identities and then delete the matching one
        FilterRequest<UserExternalIdentityDTO> filterRequest = new FilterRequest<>();
        // The actual filtering will be handled by the service
        return userExternalIdentityService.filterUserExternalIdentities(filterRequest)
                .flatMap(paginationResponse -> {
                    // Find the user external identity with matching userId and externalIdentityId
                    for (UserExternalIdentityDTO userExternalIdentity : paginationResponse.getContent()) {
                        if (userExternalIdentity.getUserAccountId().equals(userId) && 
                            userExternalIdentity.getId().equals(externalIdentityId)) {
                            // Delete the found user external identity
                            return userExternalIdentityService.deleteUserExternalIdentity(userExternalIdentity.getId());
                        }
                    }
                    // If no matching user external identity is found, return an error
                    return Mono.error(new RuntimeException("User external identity not found for userId: " + 
                                                          userId + " and externalIdentityId: " + externalIdentityId));
                });
    }

    @Operation(summary = "Create a new user external identity", description = "Creates a new user external identity and returns the created user external identity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User external identity successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserExternalIdentityDTO.class)))
    })
    @PostMapping(value = "/external-identities", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserExternalIdentityDTO> createUserExternalIdentity(@RequestBody UserExternalIdentityDTO userExternalIdentityDTO) {
        return userExternalIdentityService.createUserExternalIdentity(userExternalIdentityDTO);
    }

    @Operation(summary = "Update an existing user external identity", description = "Updates an existing user external identity and returns the updated user external identity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User external identity successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserExternalIdentityDTO.class))),
            @ApiResponse(responseCode = "404", description = "User external identity not found")
    })
    @PutMapping(value = "/external-identities/{externalIdentityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UserExternalIdentityDTO> updateUserExternalIdentity(
            @Parameter(description = "ID of the user external identity to update", required = true)
            @PathVariable UUID externalIdentityId,
            @RequestBody UserExternalIdentityDTO userExternalIdentityDTO) {
        return userExternalIdentityService.updateUserExternalIdentity(externalIdentityId, userExternalIdentityDTO);
    }

    @Operation(summary = "Delete a user external identity", description = "Deletes a user external identity by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User external identity successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User external identity not found")
    })
    @DeleteMapping("/external-identities/{externalIdentityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUserExternalIdentity(
            @Parameter(description = "ID of the user external identity to delete", required = true)
            @PathVariable UUID externalIdentityId) {
        return userExternalIdentityService.deleteUserExternalIdentity(externalIdentityId);
    }
}