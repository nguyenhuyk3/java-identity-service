package com.identity.identity_service.services;

import com.identity.identity_service.dto.requests.RoleRequest;
import com.identity.identity_service.dto.responses.RoleResponse;
import com.identity.identity_service.mappers.RoleMapper;
import com.identity.identity_service.repositories.PermissionRepository;
import com.identity.identity_service.repositories.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse createNewRole(RoleRequest req){
        var role = roleMapper.toRole(req);
        var permissions = permissionRepository.findAllById(req.getPermissions());

        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAllRoles(){
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void deleteRole(String role){
        roleRepository.deleteById(role);
    }
}
