package com.identity.identity_service.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.identity.identity_service.dto.requests.RoleRequest;
import com.identity.identity_service.dto.responses.APIResponse;
import com.identity.identity_service.dto.responses.RoleResponse;
import com.identity.identity_service.services.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    APIResponse<RoleResponse> createNewRole(@RequestBody RoleRequest req) {
        return APIResponse.<RoleResponse>builder()
                .result(roleService.createNewRole(req))
                .build();
    }

    @GetMapping
    APIResponse<List<RoleResponse>> getAllRoles() {
        return APIResponse.<List<RoleResponse>>builder()
                .result(roleService.getAllRoles())
                .build();
    }

    @DeleteMapping("/{role}")
    APIResponse<Void> deleteRole(@PathVariable String role) {
        roleService.deleteRole(role);

        return APIResponse.<Void>builder().build();
    }
}
