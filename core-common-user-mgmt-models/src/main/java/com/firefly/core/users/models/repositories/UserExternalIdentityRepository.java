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

import com.firefly.core.users.models.entities.UserExternalIdentity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository interface for UserExternalIdentity entity.
 * Extends BaseRepository to inherit common CRUD operations.
 */
@Repository
public interface UserExternalIdentityRepository extends BaseRepository<UserExternalIdentity, UUID> {

    /**
     * Find external identities by user account ID.
     *
     * @param userAccountId the user account ID
     * @return a Flux of UserExternalIdentity entities
     */
    Flux<UserExternalIdentity> findByUserAccountId(UUID userAccountId);
    
    /**
     * Find external identities by provider.
     *
     * @param provider the provider
     * @return a Flux of UserExternalIdentity entities
     */
    Flux<UserExternalIdentity> findByProvider(String provider);
    
    /**
     * Find an external identity by provider and subject ID.
     *
     * @param provider the provider
     * @param subjectId the subject ID
     * @return a Mono of UserExternalIdentity entity
     */
    Mono<UserExternalIdentity> findByProviderAndSubjectId(String provider, String subjectId);
    
    /**
     * Find an external identity by user account ID and is primary flag.
     *
     * @param userAccountId the user account ID
     * @param isPrimary the is primary flag
     * @return a Mono of UserExternalIdentity entity
     */
    Mono<UserExternalIdentity> findByUserAccountIdAndIsPrimary(Long userAccountId, Boolean isPrimary);
    
    /**
     * Delete external identities by user account ID.
     *
     * @param userAccountId the user account ID
     * @return a Mono of Void
     */
    Mono<Void> deleteByUserAccountId(Long userAccountId);
}