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

import com.firefly.core.users.models.entities.UserRole;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository interface for UserRole entity.
 * Extends BaseRepository to inherit common CRUD operations.
 */
@Repository
public interface UserRoleRepository extends BaseRepository<UserRole, UUID> {

    /**
     * Find user roles by user account ID.
     *
     * @param userAccountId the user account ID
     * @return a Flux of UserRole entities
     */
    Flux<UserRole> findByUserAccountId(UUID userAccountId);

    /**
     * Find user roles by role ID.
     *
     * @param roleId the role ID
     * @return a Flux of UserRole entities
     */
    Flux<UserRole> findByRoleId(UUID roleId);

    /**
     * Find user roles by branch ID.
     *
     * @param branchId the branch ID
     * @return a Flux of UserRole entities
     */
    Flux<UserRole> findByBranchId(UUID branchId);
    
    /**
     * Find user roles by distributor ID.
     *
     * @param distributorId the distributor ID
     * @return a Flux of UserRole entities
     */
    Flux<UserRole> findByDistributorId(Long distributorId);
    
    /**
     * Find a user role by user account ID, role ID, branch ID, and distributor ID.
     *
     * @param userAccountId the user account ID
     * @param roleId the role ID
     * @param branchId the branch ID
     * @param distributorId the distributor ID
     * @return a Mono of UserRole entity
     */
    Mono<UserRole> findByUserAccountIdAndRoleIdAndBranchIdAndDistributorId(
            Long userAccountId, Long roleId, Long branchId, Long distributorId);
    
    /**
     * Delete user roles by user account ID.
     *
     * @param userAccountId the user account ID
     * @return a Mono of Void
     */
    Mono<Void> deleteByUserAccountId(Long userAccountId);
    
    /**
     * Delete user roles by role ID.
     *
     * @param roleId the role ID
     * @return a Mono of Void
     */
    Mono<Void> deleteByRoleId(Long roleId);
}