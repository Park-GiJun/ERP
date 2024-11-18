package com.gijun.erp.controller.auth;

import com.gijun.erp.common.response.ApiResponse;
import com.gijun.erp.dto.auth.AuthDto;
import com.gijun.erp.dto.auth.AuthDto.SignUpRequest;
import com.gijun.erp.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "인증 관련 API")
@RestController
@RequestMapping("/api/public/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원 가입", description = "새로운 사용자를 등록합니다")
    @PostMapping("/signup")
    public ApiResponse<Void> signUp(
            @Parameter(description = "회원가입 정보", required = true)
            @Valid @RequestBody SignUpRequest request
    ) {
        authService.signUp(request);
        return ApiResponse.successResponse();
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다")
    @PostMapping("/login")
    public ApiResponse<AuthDto.LoginResponse> login(
            @Valid @RequestBody AuthDto.LoginRequest request
    ) {
        return ApiResponse.success(authService.login(request));
    }

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급받습니다")
    @PostMapping("/refresh")
    public ApiResponse<AuthDto.TokenResponse> refresh(
            @Valid @RequestBody AuthDto.TokenRequest request
    ) {
        return ApiResponse.success(authService.refresh(request));
    }
}