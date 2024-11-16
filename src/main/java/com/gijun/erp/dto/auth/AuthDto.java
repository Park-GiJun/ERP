package com.gijun.erp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AuthDto {

    @Schema(description = "회원가입 요청")
    public record SignUpRequest(
            @Schema(description = "이메일", example = "user@example.com")
            @NotBlank(message = "이메일은 필수 입력값입니다")
            @Email(message = "올바른 이메일 형식이 아닙니다")
            String email,

            @Schema(description = "비밀번호", example = "Password123!")
            @NotBlank(message = "비밀번호는 필수 입력값입니다")
            @Pattern(
                    regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
                    message = "비밀번호는 8자 이상의 영문, 숫자, 특수문자를 포함해야 합니다"
            )
            String password,

            @Schema(description = "비밀번호 확인", example = "Password123!")
            @NotBlank(message = "비밀번호 확인은 필수 입력값입니다")
            String confirmPassword,

            @Schema(description = "이름", example = "홍길동")
            @NotBlank(message = "이름은 필수 입력값입니다")
            @Size(min = 2, max = 30, message = "이름은 2자 이상 30자 이하이어야 합니다")
            String name,

            @Schema(description = "사원번호", example = "EMP001")
            @Pattern(regexp = "^EMP\\d{3,}$", message = "올바른 사원번호 형식이 아닙니다")
            String employeeNumber,

            @Schema(description = "전화번호", example = "010-1234-5678")
            @Pattern(
                    regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
                    message = "올바른 전화번호 형식이 아닙니다"
            )
            String phoneNumber,

            @Schema(description = "부서 ID", example = "1")
            Long departmentId,

            @Schema(description = "직급 ID", example = "1")
            Long positionId
    ) {}
}