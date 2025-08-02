package com.identity.identity_service.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
/*
    @Builder là một annotation của Lombok giúp bạn
tự động tạo "Builder pattern" cho class Java — cho phép bạn khởi tạo đối tượng một cách linh hoạt,
dễ đọc và không cần constructor dài dòng.
*/
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class APIResponse<T> {
    private T result;
    private int code = 1000;
    private String message;
}
