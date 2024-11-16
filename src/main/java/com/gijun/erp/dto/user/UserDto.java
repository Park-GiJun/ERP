package com.gijun.erp.dto.user;

import com.gijun.erp.domain.user.UserRole;
import com.gijun.erp.domain.user.UserStatus;
import java.time.LocalDateTime;

public class UserDto {
    public record UserResponse(
            Long id,
            String email,
            String name,
            String employeeNumber,
            String phoneNumber,
            UserRole role,
            UserStatus status,
            Long departmentId,
            Long positionId,
            LocalDateTime lastLoginAt,
            LocalDateTime passwordChangedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    public record UserCreateRequest(
            String email,
            String password,
            String name,
            String employeeNumber,
            String phoneNumber,
            UserRole role,
            UserStatus status,
            Long departmentId,
            Long positionId
    ) {}

    public record UserUpdateRequest(
            String name,
            String phoneNumber,
            Long departmentId,
            Long positionId
    ) {}

    public record UserPasswordChangeRequest(
            String currentPassword,
            String newPassword,
            String confirmPassword
    ) {}

    public record UserStatusUpdateRequest(
            UserStatus status
    ) {}

    public record UserSearchCondition(
            String email,
            String name,
            String employeeNumber,
            UserRole role,
            UserStatus status,
            Long departmentId,
            Long positionId,
            Boolean deleted
    ) {}
}
