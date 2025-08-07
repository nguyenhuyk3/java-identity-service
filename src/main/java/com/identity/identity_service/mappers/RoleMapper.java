package com.identity.identity_service.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.identity.identity_service.dto.requests.RoleRequest;
import com.identity.identity_service.dto.responses.RoleResponse;
import com.identity.identity_service.entities.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    /*
    	@Mapping(target = "permissions", ignore = true)
    	Nó có nghĩa là: "Bỏ qua trường permissions trong đối tượng Role khi ánh xạ từ RoleRequest."
    */
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
