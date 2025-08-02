package com.identity.identity_service.exception;

import com.identity.identity_service.dto.response.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
/*
    Đánh dấu class này là "bộ xử lý ngoại lệ toàn cục" (global exception handler) cho tất cả các controller trong app.
    Spring sẽ tự động bắt các lỗi được ném ra trong controller và chuyển vào đây xử lý.
*/
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

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<APIResponse> handleRuntimeException(RuntimeException ex) {
        APIResponse res = new APIResponse();

        res.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        res.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<APIResponse> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        APIResponse res = new APIResponse();

        res.setCode(errorCode.getCode());
        res.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(res);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse> handlingValidation(MethodArgumentNotValidException ex) {
        String enumKey = ex.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {

        }

        APIResponse res = APIResponse
                                .builder()
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .build();

        return ResponseEntity.badRequest().body(res);
    }
}
