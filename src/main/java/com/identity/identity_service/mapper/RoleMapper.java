package com.identity.identity_service.mapper;


import com.identity.identity_service.dto.request.RoleRequest;
import com.identity.identity_service.dto.response.RoleResponse;
import com.identity.identity_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
