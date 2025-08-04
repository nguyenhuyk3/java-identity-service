package com.identity.identity_service.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {DOBValidator.class})
/*
    - @Target({FIELD})
        + Annotation này chỉ được dùng trên các field (biến trong class).
    - @Retention(RUNTIME)
        + Cho biết annotation này sẽ được giữ lại ở thời điểm runtime – tức là bạn có thể kiểm tra nó bằng reflection lúc chương trình đang chạy.
        + Đây là bắt buộc cho annotation dùng với Bean Validation (vì validator cần đọc metadata khi chạy).
    - @Constraint(validatedBy = {DOBValidator.class})
        + Nói rằng: annotation này sẽ được xử lý bởi class DOBValidator.
*/
public @interface DOBConstraint {
    String message() default "Invalid date of birth";

    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
