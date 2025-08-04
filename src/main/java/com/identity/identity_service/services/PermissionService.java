package com.identity.identity_service.services;

import com.identity.identity_service.dto.requests.PermissionRequest;
import com.identity.identity_service.dto.responses.PermissionResponse;
import com.identity.identity_service.entities.Permission;
import com.identity.identity_service.mappers.PermissionMapper;
import com.identity.identity_service.repositories.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse createNewPermission(PermissionRequest req) {
        Permission permission = permissionMapper.toPermission(req);

        permissionRepository.save(permission);

        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAllPermissions() {
        var permissions = permissionRepository.findAll();

        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void deletePermission(String permission) {
        permissionRepository.deleteById(permission);
    }
}
