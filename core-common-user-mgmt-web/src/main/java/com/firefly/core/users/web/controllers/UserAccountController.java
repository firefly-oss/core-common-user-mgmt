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
import com.firefly.core.users.core.services.UserAccountService;
import com.firefly.core.users.interfaces.dtos.UserAccountDTO;
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
@RequestMapping("/api/v1/users")
@Tag(name = "User Accounts", description = "API for managing user accounts")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @Operation(summary = "Get all user accounts with filtering", description = "Returns a paginated list of user accounts based on filter criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user accounts",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class)))
    })
    @PostMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PaginationResponse<UserAccountDTO>> filterUserAccounts(@RequestBody FilterRequest<UserAccountDTO> filterRequest) {
        return userAccountService.filterUserAccounts(filterRequest);
    }

    @Operation(summary = "Get user account by ID", description = "Returns a user account by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user account",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserAccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "User account not found")
    })
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UserAccountDTO> getUserAccountById(
            @Parameter(description = "ID of the user account to retrieve", required = true)
            @PathVariable UUID userId) {
        return userAccountService.getUserAccountById(userId);
    }

    @Operation(summary = "Create a new user account", description = "Creates a new user account and returns the created user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User account successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserAccountDTO.class)))
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserAccountDTO> createUserAccount(@RequestBody UserAccountDTO userAccountDTO) {
        return userAccountService.createUserAccount(userAccountDTO);
    }

    @Operation(summary = "Update an existing user account", description = "Updates an existing user account and returns the updated user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User account successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserAccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "User account not found")
    })
    @PutMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UserAccountDTO> updateUserAccount(
            @Parameter(description = "ID of the user account to update", required = true)
            @PathVariable UUID userId,
            @RequestBody UserAccountDTO userAccountDTO) {
        return userAccountService.updateUserAccount(userId, userAccountDTO);
    }

    @Operation(summary = "Delete a user account", description = "Deletes a user account by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User account successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User account not found")
    })
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUserAccount(
            @Parameter(description = "ID of the user account to delete", required = true)
            @PathVariable UUID userId) {
        return userAccountService.deleteUserAccount(userId);
    }
}