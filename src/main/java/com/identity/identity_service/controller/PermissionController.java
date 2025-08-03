package com.identity.identity_service.controller;

import com.identity.identity_service.dto.request.PermissionRequest;
import com.identity.identity_service.dto.response.APIResponse;
import com.identity.identity_service.dto.response.PermissionResponse;
import com.identity.identity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    APIResponse<PermissionResponse> createNewPermission(@RequestBody PermissionRequest req){
        return APIResponse.<PermissionResponse>builder()
                .result(permissionService.createNewPermission(req))
                .build();
    }

    @GetMapping
    APIResponse<List<PermissionResponse>> getAllPermissions(){
        return APIResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAllPermissions())
                .build();
    }

    @DeleteMapping("/{permission}")
    APIResponse<Void> deletePermission(@PathVariable String permission){
        permissionService.deletePermission(permission);

        return APIResponse.<Void>builder().build();
    }
}
