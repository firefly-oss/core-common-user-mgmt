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


package com.firefly.core.users.models.repositories;

import com.firefly.core.users.models.entities.AuditLog;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * Repository interface for AuditLog entity.
 * Extends BaseRepository to inherit common CRUD operations.
 */
@Repository
public interface AuditLogRepository extends BaseRepository<AuditLog, UUID> {

    /**
     * Find audit logs by user account ID.
     *
     * @param userAccountId the user account ID
     * @return a Flux of AuditLog entities
     */
    Flux<AuditLog> findByUserAccountId(UUID userAccountId);
    
    /**
     * Find audit logs by action.
     *
     * @param action the action performed
     * @return a Flux of AuditLog entities
     */
    Flux<AuditLog> findByAction(String action);
    
    /**
     * Find audit logs by resource.
     *
     * @param resource the resource affected
     * @return a Flux of AuditLog entities
     */
    Flux<AuditLog> findByResource(String resource);
}