package com.identity.identity_service.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.identity.identity_service.dto.requests.PermissionRequest;
import com.identity.identity_service.dto.responses.APIResponse;
import com.identity.identity_service.dto.responses.PermissionResponse;
import com.identity.identity_service.services.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    APIResponse<PermissionResponse> createNewPermission(@RequestBody PermissionRequest req) {
        return APIResponse.<PermissionResponse>builder()
                .result(permissionService.createNewPermission(req))
                .build();
    }

    @GetMapping
    APIResponse<List<PermissionResponse>> getAllPermissions() {
        return APIResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAllPermissions())
                .build();
    }

    @DeleteMapping("/{permission}")
    APIResponse<Void> deletePermission(@PathVariable String permission) {
        permissionService.deletePermission(permission);

        return APIResponse.<Void>builder().build();
    }
}
