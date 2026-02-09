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
import com.firefly.core.users.core.services.AuditLogService;
import com.firefly.core.users.interfaces.dtos.AuditLogDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audit-logs")
@Tag(name = "Audit Logs", description = "API for accessing audit logs")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @Operation(summary = "Get all audit logs with filtering", description = "Returns a paginated list of audit logs based on filter criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved audit logs",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class)))
    })
    @PostMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PaginationResponse<AuditLogDTO>> filterAuditLogs(@RequestBody FilterRequest<AuditLogDTO> filterRequest) {
        return auditLogService.filterAuditLogs(filterRequest);
    }

    @Operation(summary = "Get audit log by ID", description = "Returns an audit log by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved audit log",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuditLogDTO.class))),
            @ApiResponse(responseCode = "404", description = "Audit log not found")
    })
    @GetMapping(value = "/{auditLogId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AuditLogDTO> getAuditLogById(
            @Parameter(description = "ID of the audit log to retrieve", required = true)
            @PathVariable UUID auditLogId) {
        return auditLogService.getAuditLogById(auditLogId);
    }

    @Operation(summary = "Get audit logs for a user", description = "Returns all audit logs for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved audit logs",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class)))
    })
    @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PaginationResponse<AuditLogDTO>> getAuditLogsByUserId(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId) {
        FilterRequest<AuditLogDTO> filterRequest = new FilterRequest<>();
        // The actual filtering will be handled by the service based on the userId
        return auditLogService.filterAuditLogs(filterRequest);
    }

    @Operation(summary = "Get audit logs for a resource", description = "Returns all audit logs for a specific resource")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved audit logs",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginationResponse.class)))
    })
    @GetMapping(value = "/resources/{resourceType}/{resourceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PaginationResponse<AuditLogDTO>> getAuditLogsByResource(
            @Parameter(description = "Type of the resource", required = true)
            @PathVariable String resourceType,
            @Parameter(description = "ID of the resource", required = true)
            @PathVariable String resourceId) {
        FilterRequest<AuditLogDTO> filterRequest = new FilterRequest<>();
        // The actual filtering will be handled by the service based on the resource type and ID
        return auditLogService.filterAuditLogs(filterRequest);
    }
}