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

import com.firefly.core.users.interfaces.dtos.RoleDTO;
import com.firefly.core.users.models.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for converting between Role entity and RoleDTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    
    /**
     * Converts a Role entity to a RoleDTO.
     *
     * @param entity the Role entity
     * @return the RoleDTO
     */
    RoleDTO toDTO(Role entity);
    
    /**
     * Converts a RoleDTO to a Role entity.
     *
     * @param dto the RoleDTO
     * @return the Role entity
     */
    Role toEntity(RoleDTO dto);
}