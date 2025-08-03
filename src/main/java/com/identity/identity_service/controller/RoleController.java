package com.identity.identity_service.controller;


import com.identity.identity_service.dto.request.RoleRequest;
import com.identity.identity_service.dto.response.APIResponse;
import com.identity.identity_service.dto.response.RoleResponse;
import com.identity.identity_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    APIResponse<RoleResponse> createNewRole(@RequestBody RoleRequest req){
        return APIResponse.<RoleResponse>builder()
                .result(roleService.createNewRole(req))
                .build();
    }

    @GetMapping
    APIResponse<List<RoleResponse>> getAllRoles(){
        return APIResponse.<List<RoleResponse>>builder()
                .result(roleService.getAllRoles())
                .build();
    }

    @DeleteMapping("/{role}")
    APIResponse<Void> deleteRole(@PathVariable String role){
        roleService.deleteRole(role);

        return APIResponse.<Void>builder().build();
    }
}
