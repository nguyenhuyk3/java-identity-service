package com.identity.identity_service.exceptions;

import com.identity.identity_service.dto.responses.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.swing.*;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
/*
    Đánh dấu class này là "bộ xử lý ngoại lệ toàn cục" (global exception handler) cho tất cả các controller trong app.
    Spring sẽ tự động bắt các lỗi được ném ra trong controller và chuyển vào đây xử lý.
*/
@Slf4j
public class GlobalExceptionHandler {
//    @ExceptionHandler(value = RuntimeException.class)
    /*
        @ExceptionHandler(RuntimeException.class) nghĩa là: nếu trong controller xảy ra lỗi RuntimeException
    (hoặc các class con như IllegalArgumentException, NullPointerException...), thì method này sẽ xử lý.
    */
//    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
//        return ResponseEntity.badRequest().body(ex.getMessage());
//    }

//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    /*
        MethodArgumentNotValidException xảy ra khi bạn dùng @Valid
    trong controller mà dữ liệu không hợp lệ (ví dụ thiếu trường, sai format...).
        Trong method này, bạn đang trả về thông điệp mặc định ex.getMessage() – nhưng nó khá khó đọc.
    */
//    ResponseEntity<String> handeValidation(MethodArgumentNotValidException ex) {
//        return ResponseEntity.badRequest().body(ex.getMessage());
//    }
//    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//
//        ex.getBindingResult().getFieldErrors().forEach(error -> {
//            errors.put(error.getField(), error.getDefaultMessage());
//        });
//
//        return ResponseEntity.badRequest().body(errors);
//    }

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<APIResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .badRequest()
                .body(APIResponse
                        .builder()
                        .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                        .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                        .build());
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<APIResponse> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        APIResponse res = new APIResponse();

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(APIResponse
                        .builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<APIResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(APIResponse
                        .builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse> handleValidation(MethodArgumentNotValidException ex) {
        String enumKey = ex.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;

        try {
            errorCode = ErrorCode.valueOf(enumKey);

//            var constraintViolation = ex
//                    .getBindingResult()
//                    .getAllErrors()
//                    .getFirst()
//                    .unwrap(SpringLayout.Constraints.class);
        } catch (IllegalArgumentException e) {

        }

        return ResponseEntity.badRequest().body(APIResponse
                .builder()
                .code(errorCode.getCode())
                .message(Objects.nonNull(attributes) ? mapAttribute(errorCode.getMessage(), attributes) : errorCode.getMessage())
                .build());
    }
}
