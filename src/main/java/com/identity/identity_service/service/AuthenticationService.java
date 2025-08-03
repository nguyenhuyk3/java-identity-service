package com.identity.identity_service.service;

import com.identity.identity_service.dto.request.AuthenticationRequest;
import com.identity.identity_service.dto.request.IntrospectRequest;
import com.identity.identity_service.dto.response.APIResponse;
import com.identity.identity_service.dto.response.AuthenticationResponse;
import com.identity.identity_service.dto.response.IntrospectResponse;
import com.identity.identity_service.entity.User;
import com.identity.identity_service.exception.AppException;
import com.identity.identity_service.exception.ErrorCode;
import com.identity.identity_service.repository.UserRepository;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jose.Payload;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    /*
        Annotation @Value("${jwt.signerKey}") là một phần của Spring Framework,
    dùng để inject giá trị cấu hình từ file application.properties hoặc application.yml vào biến trong class Java.
    */
    protected String SIGNER_KEY;

    @NonFinal
    @Value("{jwt.issuer}")
    protected String ISSUER;

//    public boolean authenticate(AuthenticationRequest req) {
//        var user = userRepository.findByUsername(req.getUsername())
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//
//        return passwordEncoder.matches(req.getPassword(), user.getPassword());
//    }

    public IntrospectResponse introspect(IntrospectRequest req) throws JOSEException, ParseException {
        var token = req.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest req) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated = passwordEncoder.matches(req.getPassword(),
                user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(stringJoiner::add);
        }

        return stringJoiner.toString();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                // Gán Username vào trường "sub" trong jwt token.
                .subject(user.getUsername())
                .issuer(ISSUER)
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant
                                .now()
                                .plus(1, ChronoUnit.HOURS)
                                .toEpochMilli()
                ))
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token: ", e);

            throw new RuntimeException(e);
        }
    }
}
