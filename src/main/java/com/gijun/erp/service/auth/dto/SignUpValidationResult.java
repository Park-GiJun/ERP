package com.gijun.erp.service.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpValidationResult {
    private final boolean isValid;
    private final String errorCode;
    private final String errorMessage;

    public static SignUpValidationResult success() {
        return SignUpValidationResult.builder()
                .isValid(true)
                .build();
    }

    public static SignUpValidationResult failure(String errorCode, String errorMessage) {
        return SignUpValidationResult.builder()
                .isValid(false)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }
}