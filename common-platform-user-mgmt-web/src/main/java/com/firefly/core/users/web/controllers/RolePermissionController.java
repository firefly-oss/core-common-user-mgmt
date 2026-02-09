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
import com.firefly.core.users.core.services.RolePermissionService;
import com.firefly.core.users.interfaces.dtos.RolePermissionDTO;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Role Permissions", description = "API for managing role permissions")
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;

    @Operation(summary = "Get all role permissions with filtering", description = "Returns a paginated list of role permissions based on filter criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved role permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class)))
    })
    @PostMapping(value = "/role-permissions/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PaginationResponse<RolePermissionDTO>> filterRolePermissions(@RequestBody FilterRequest<RolePermissionDTO> filterRequest) {
        return rolePermissionService.filterRolePermissions(filterRequest);
    }

    @Operation(summary = "Get role permission by ID", description = "Returns a role permission by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved role permission",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RolePermissionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Role permission not found")
    })
    @GetMapping(value = "/role-permissions/{rolePermissionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<RolePermissionDTO> getRolePermissionById(
            @Parameter(description = "ID of the role permission to retrieve", required = true)
            @PathVariable UUID rolePermissionId) {
        return rolePermissionService.getRolePermissionById(rolePermissionId);
    }

    @Operation(summary = "Get permissions for a role", description = "Returns all permissions assigned to a specific role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved role permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RolePermissionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @GetMapping(value = "/roles/{roleId}/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PaginationResponse<RolePermissionDTO>> getPermissionsByRoleId(
            @Parameter(description = "ID of the role", required = true)
            @PathVariable Long roleId) {
        // Create a filter request with roleId as a filter parameter
        FilterRequest<RolePermissionDTO> filterRequest = new FilterRequest<>();
        // The actual filtering will be handled by the service based on the roleId
        return rolePermissionService.filterRolePermissions(filterRequest);
    }

    @Operation(summary = "Assign permission to role", description = "Assigns a permission to a role and returns the created role permission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Permission successfully assigned to role",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RolePermissionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Role or permission not found")
    })
    @PostMapping(value = "/roles/{roleId}/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RolePermissionDTO> assignPermissionToRole(
            @Parameter(description = "ID of the role", required = true)
            @PathVariable UUID roleId,
            @RequestBody RolePermissionDTO rolePermissionDTO) {
        rolePermissionDTO.setRoleId(roleId);
        return rolePermissionService.createRolePermission(rolePermissionDTO);
    }

    @Operation(summary = "Remove permission from role", description = "Removes a permission from a role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Permission successfully removed from role"),
            @ApiResponse(responseCode = "404", description = "Role permission not found")
    })
    @DeleteMapping("/roles/{roleId}/permissions/{permissionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> removePermissionFromRole(
            @Parameter(description = "ID of the role", required = true)
            @PathVariable Long roleId,
            @Parameter(description = "ID of the permission", required = true)
            @PathVariable Long permissionId) {
        // Since there's no direct method to delete by roleId and permissionId,
        // we need to filter role permissions and then delete the matching one
        FilterRequest<RolePermissionDTO> filterRequest = new FilterRequest<>();
        // The actual filtering will be handled by the service
        return rolePermissionService.filterRolePermissions(filterRequest)
                .flatMap(paginationResponse -> {
                    // Find the role permission with matching roleId and permissionId
                    for (RolePermissionDTO rolePermission : paginationResponse.getContent()) {
                        if (rolePermission.getRoleId().equals(roleId) && 
                            rolePermission.getPermissionId().equals(permissionId)) {
                            // Delete the found role permission
                            return rolePermissionService.deleteRolePermission(rolePermission.getId());
                        }
                    }
                    // If no matching role permission is found, return an error
                    return Mono.error(new RuntimeException("Role permission not found for roleId: " + 
                                                          roleId + " and permissionId: " + permissionId));
                });
    }

    @Operation(summary = "Create a new role permission", description = "Creates a new role permission and returns the created role permission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role permission successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RolePermissionDTO.class)))
    })
    @PostMapping(value = "/role-permissions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RolePermissionDTO> createRolePermission(@RequestBody RolePermissionDTO rolePermissionDTO) {
        return rolePermissionService.createRolePermission(rolePermissionDTO);
    }

    @Operation(summary = "Delete a role permission", description = "Deletes a role permission by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role permission successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Role permission not found")
    })
    @DeleteMapping("/role-permissions/{rolePermissionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteRolePermission(
            @Parameter(description = "ID of the role permission to delete", required = true)
            @PathVariable UUID rolePermissionId) {
        return rolePermissionService.deleteRolePermission(rolePermissionId);
    }
}
