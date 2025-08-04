package com.identity.identity_service.mappers;

import com.identity.identity_service.dto.requests.PermissionRequest;
import com.identity.identity_service.dto.responses.PermissionResponse;
import com.identity.identity_service.entities.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest req);
    PermissionResponse toPermissionResponse(Permission permission);
}
