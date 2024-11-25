package com.gijun.erp.dto.attendance;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class AnnualLeaveDto {

    public record CreateRequest(
            @NotNull(message = "사용자 ID는 필수입니다")
            Long userId,

            @NotNull(message = "연도는 필수입니다")
            Integer year,

            @NotNull(message = "총 연차 일수는 필수입니다")
            @Positive(message = "총 연차 일수는 0보다 커야 합니다")
            Double totalDays
    ) {}

    public record Response(
            Long id,
            Long userId,
            String userName,
            Integer year,
            Double totalDays,
            Double usedDays,
            Double remainingDays,
            LocalDate expirationDate,
            boolean expired,
            LocalDate createdAt,
            LocalDate updatedAt
    ) {}
}