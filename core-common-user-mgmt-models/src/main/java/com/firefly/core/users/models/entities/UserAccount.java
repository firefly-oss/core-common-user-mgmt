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


package com.firefly.core.users.models.entities;

import com.firefly.core.users.interfaces.enums.ThemePreferenceEnum;
import com.firefly.core.users.interfaces.enums.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Entity representing a user account in the system.
 * Maps to the user_account table in the database.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("user_account")
public class UserAccount {
    
    @Id
    private UUID id;

    @Column("full_name")
    private String fullName;

    private String nickname;

    private String email;

    @Column("user_type")
    private UserTypeEnum userType;

    @Column("branch_id")
    private UUID branchId;

    @Column("distributor_id")
    private UUID distributorId;

    @Column("department_id")
    private UUID departmentId;

    @Column("position_id")
    private UUID positionId;
    
    @Column("job_title")
    private String jobTitle;
    
    @Column("avatar_url")
    private String avatarUrl;
    
    @Column("theme_preference")
    private ThemePreferenceEnum themePreference;
    
    @Column("language_preference")
    private String languagePreference;
    
    private String locale;
    
    private String timezone;
    
    @Column("contact_phone")
    private String contactPhone;
    
    @Column("is_active")
    private Boolean isActive;
    
    @CreatedDate
    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("created_by")
    private UUID createdBy;

    @LastModifiedDate
    @Column("updated_at")
    private OffsetDateTime updatedAt;

    @Column("updated_by")
    private UUID updatedBy;
}