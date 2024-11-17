package com.gijun.erp.dto.attendance;

import com.gijun.erp.domain.attendance.enums.AttendanceStatus;
import java.time.LocalDateTime;

public class AttendanceDto {
    public record AttendanceResponse(
            Long id,
            Long userId,
            String userName,
            LocalDateTime workDate,
            LocalDateTime checkIn,
            LocalDateTime checkOut,
            AttendanceStatus status,
            String note,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    public record CheckInRequest(
            String note
    ) {}

    public record CheckOutRequest(
            String note
    ) {}

    public record AttendanceUpdateRequest(
            AttendanceStatus status,
            String note
    ) {}

    public record AttendanceSearchCondition(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            AttendanceStatus status
    ) {}
}
