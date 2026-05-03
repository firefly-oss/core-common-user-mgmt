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


package com.firefly.core.users.interfaces.dtos;

import com.firefly.core.users.interfaces.enums.ThemePreferenceEnum;
import com.firefly.core.users.interfaces.enums.UserTypeEnum;
import org.fireflyframework.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO representing a user account in the system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotBlank(message = "Full name is required")
    @Size(min = 1, max = 255, message = "Full name must be between 1 and 255 characters")
    private String fullName;

    @Size(max = 100, message = "Nickname must not exceed 100 characters")
    private String nickname;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @NotNull(message = "User type is required")
    private UserTypeEnum userType;

    @FilterableId
    private UUID branchId;

    @FilterableId
    private UUID distributorId;

    @FilterableId
    private UUID departmentId;

    @FilterableId
    private UUID positionId;

    @Size(max = 255, message = "Job title must not exceed 255 characters")
    private String jobTitle;

    @Size(max = 500, message = "Avatar URL must not exceed 500 characters")
    @Pattern(regexp = "^(https?://)?[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?$",
             message = "Avatar URL must be a valid URL format",
             flags = Pattern.Flag.CASE_INSENSITIVE)
    private String avatarUrl;

    private ThemePreferenceEnum themePreference;

    @Size(max = 10, message = "Language preference must not exceed 10 characters")
    @Pattern(regexp = "^[a-z]{2}(-[A-Z]{2})?$", message = "Language preference must be in format 'en' or 'en-US'")
    private String languagePreference;

    @Size(max = 10, message = "Locale must not exceed 10 characters")
    @Pattern(regexp = "^[a-z]{2}_[A-Z]{2}$", message = "Locale must be in format 'en_US'")
    private String locale;

    @Size(max = 50, message = "Timezone must not exceed 50 characters")
    private String timezone;

    @Size(max = 20, message = "Contact phone must not exceed 20 characters")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Contact phone must be a valid phone number")
    private String contactPhone;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    private OffsetDateTime createdAt;
    private UUID createdBy;
    private OffsetDateTime updatedAt;
    private UUID updatedBy;
}