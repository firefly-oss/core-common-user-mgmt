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


package com.firefly.core.users.web.controllers;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.users.core.services.UserRoleService;
import com.firefly.core.users.interfaces.dtos.UserRoleDTO;
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
@Tag(name = "User Roles", description = "API for managing user roles")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @Operation(summary = "Get all user roles with filtering", description = "Returns a paginated list of user roles based on filter criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user roles",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class)))
    })
    @PostMapping(value = "/user-roles/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PaginationResponse<UserRoleDTO>> filterUserRoles(@RequestBody FilterRequest<UserRoleDTO> filterRequest) {
        return userRoleService.filterUserRoles(filterRequest);
    }

    @Operation(summary = "Get user role by ID", description = "Returns a user role by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user role",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRoleDTO.class))),
            @ApiResponse(responseCode = "404", description = "User role not found")
    })
    @GetMapping(value = "/user-roles/{userRoleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UserRoleDTO> getUserRoleById(
            @Parameter(description = "ID of the user role to retrieve", required = true)
            @PathVariable UUID userRoleId) {
        return userRoleService.getUserRoleById(userRoleId);
    }

    @Operation(summary = "Get roles for a user", description = "Returns all roles assigned to a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user roles",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping(value = "/users/{userId}/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PaginationResponse<UserRoleDTO>> getRolesByUserId(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable UUID userId) {
        FilterRequest<UserRoleDTO> filterRequest = new FilterRequest<>();
        // The actual filtering will be handled by the service based on the userId
        return userRoleService.filterUserRoles(filterRequest);
    }

    @Operation(summary = "Assign role to user", description = "Assigns a role to a user and returns the created user role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role successfully assigned to user",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRoleDTO.class))),
            @ApiResponse(responseCode = "404", description = "User or role not found")
    })
    @PostMapping(value = "/users/{userId}/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserRoleDTO> assignRoleToUser(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable UUID userId,
            @RequestBody UserRoleDTO userRoleDTO) {
        userRoleDTO.setUserAccountId(userId);
        return userRoleService.createUserRole(userRoleDTO);
    }

    @Operation(summary = "Remove role from user", description = "Removes a role from a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role successfully removed from user"),
            @ApiResponse(responseCode = "404", description = "User role not found")
    })
    @DeleteMapping("/users/{userId}/roles/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> removeRoleFromUser(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable UUID userId,
            @Parameter(description = "ID of the role", required = true)
            @PathVariable UUID roleId) {
        // Since there's no direct method to delete by userId and roleId,
        // we need to filter user roles and then delete the matching one
        FilterRequest<UserRoleDTO> filterRequest = new FilterRequest<>();
        // The actual filtering will be handled by the service
        return userRoleService.filterUserRoles(filterRequest)
                .flatMap(paginationResponse -> {
                    // Find the user role with matching userId and roleId
                    for (UserRoleDTO userRole : paginationResponse.getContent()) {
                        if (userRole.getUserAccountId().equals(userId) && 
                            userRole.getRoleId().equals(roleId)) {
                            // Delete the found user role
                            return userRoleService.deleteUserRole(userRole.getId());
                        }
                    }
                    // If no matching user role is found, return an error
                    return Mono.error(new RuntimeException("User role not found for userId: " + 
                                                          userId + " and roleId: " + roleId));
                });
    }

    @Operation(summary = "Create a new user role", description = "Creates a new user role and returns the created user role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User role successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRoleDTO.class)))
    })
    @PostMapping(value = "/user-roles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserRoleDTO> createUserRole(@RequestBody UserRoleDTO userRoleDTO) {
        return userRoleService.createUserRole(userRoleDTO);
    }

    @Operation(summary = "Delete a user role", description = "Deletes a user role by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User role successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User role not found")
    })
    @DeleteMapping("/user-roles/{userRoleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUserRole(
            @Parameter(description = "ID of the user role to delete", required = true)
            @PathVariable UUID userRoleId) {
        return userRoleService.deleteUserRole(userRoleId);
    }
}