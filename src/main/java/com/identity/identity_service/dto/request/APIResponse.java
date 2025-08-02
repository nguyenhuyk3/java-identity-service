package com.identity.identity_service.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class APIResponse<T> {
    private T result;
    private int code = 1000;
    private String message;
}
