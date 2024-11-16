package com.gijun.erp.dto.user;

import com.gijun.erp.domain.user.UserRole;
import com.gijun.erp.domain.user.UserStatus;

public record UserSummaryDto(
        Long id,
        String email,
        String name,
        String employeeNumber,
        UserRole role,
        UserStatus status,
        String departmentName,
        String positionName
) {}