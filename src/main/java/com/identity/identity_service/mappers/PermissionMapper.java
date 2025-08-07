package com.identity.identity_service.mappers;

import org.mapstruct.Mapper;

import com.identity.identity_service.dto.requests.PermissionRequest;
import com.identity.identity_service.dto.responses.PermissionResponse;
import com.identity.identity_service.entities.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest req);

    PermissionResponse toPermissionResponse(Permission permission);
}
