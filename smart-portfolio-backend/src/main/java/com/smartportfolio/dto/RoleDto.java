package com.smartportfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto {

    private Long id;
    private String name;
    private String description;
    private Set<PermissionDto> permissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


