package com.identity.identity_service.controllers;

import com.identity.identity_service.dto.requests.IntrospectRequest;
import com.identity.identity_service.dto.responses.APIResponse;
import com.identity.identity_service.dto.requests.AuthenticationRequest;
import com.identity.identity_service.dto.responses.AuthenticationResponse;
import com.identity.identity_service.dto.responses.IntrospectResponse;
import com.identity.identity_service.services.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

//    @PostMapping("/log-in")
//    APIResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
//        boolean result = authenticationService.authenticate(request);
//
//        return APIResponse.<AuthenticationResponse>builder()
//                .result(AuthenticationResponse
//                        .builder()
//                        .authenticated(result)
//                        .build())
//                .build();
//    }

    @PostMapping("/token")
    APIResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest req) {
        var result = authenticationService.authenticate(req);

        return APIResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    /*
        ParseException: khi token sai định dạng.
        JOSEException: khi xác minh chữ ký token thất bại.
    */
    @PostMapping("/introspect")
    APIResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest req)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(req);

        return APIResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
}
