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


package com.firefly.core.users.core.mappers;

import com.firefly.core.users.interfaces.dtos.UserRoleDTO;
import com.firefly.core.users.models.entities.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for converting between UserRole entity and UserRoleDTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRoleMapper {
    
    /**
     * Converts a UserRole entity to a UserRoleDTO.
     *
     * @param entity the UserRole entity
     * @return the UserRoleDTO
     */
    UserRoleDTO toDTO(UserRole entity);
    
    /**
     * Converts a UserRoleDTO to a UserRole entity.
     *
     * @param dto the UserRoleDTO
     * @return the UserRole entity
     */
    UserRole toEntity(UserRoleDTO dto);
}