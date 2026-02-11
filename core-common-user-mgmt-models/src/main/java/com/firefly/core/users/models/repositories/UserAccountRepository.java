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

import com.firefly.core.users.interfaces.enums.UserTypeEnum;
import com.firefly.core.users.models.entities.UserAccount;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository interface for UserAccount entity.
 * Extends BaseRepository to inherit common CRUD operations.
 */
@Repository
public interface UserAccountRepository extends BaseRepository<UserAccount, UUID> {

    /**
     * Find a user account by email.
     *
     * @param email the email
     * @return a Mono of UserAccount entity
     */
    Mono<UserAccount> findByEmail(String email);

    /**
     * Find user accounts by user type.
     *
     * @param userType the user type
     * @return a Flux of UserAccount entities
     */
    Flux<UserAccount> findByUserType(UserTypeEnum userType);

    /**
     * Find user accounts by active status.
     *
     * @param isActive the active status
     * @return a Flux of UserAccount entities
     */
    Flux<UserAccount> findByIsActive(Boolean isActive);

    /**
     * Find user accounts by branch ID.
     *
     * @param branchId the branch ID
     * @return a Flux of UserAccount entities
     */
    Flux<UserAccount> findByBranchId(UUID branchId);

    /**
     * Find user accounts by distributor ID.
     *
     * @param distributorId the distributor ID
     * @return a Flux of UserAccount entities
     */
    Flux<UserAccount> findByDistributorId(UUID distributorId);
    
    /**
     * Check if a user account with the given email exists.
     *
     * @param email the email
     * @return a Mono of Boolean
     */
    Mono<Boolean> existsByEmail(String email);
}