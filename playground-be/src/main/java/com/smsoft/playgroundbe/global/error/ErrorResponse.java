package com.smsoft.playgroundbe.global.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

@Getter @Builder
public class ErrorResponse {
    private String errorCode;
    private String errorMessage;

    public static ErrorResponse of(String errorCode, String errorMessage){
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }

    public static ErrorResponse of(String errorCode, BindingResult bindingResult) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(createErrorMessage(bindingResult))
                .build();
    }

    private static String createErrorMessage(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(fieldError -> String.format("[%s]%s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining(", "));
    }
}
