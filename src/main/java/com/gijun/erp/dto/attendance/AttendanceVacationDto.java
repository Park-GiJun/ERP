package com.gijun.erp.dto.attendance;

import com.gijun.erp.domain.attendance.enums.VacationType;
import com.gijun.erp.domain.attendance.enums.ApprovalStatus;
import java.time.LocalDateTime;

public class AttendanceVacationDto {
    public record VacationRequest(
            LocalDateTime startDate,
            LocalDateTime endDate,
            VacationType type,
            String reason
    ) {}

    public record VacationResponse(
            Long id,
            Long userId,
            String userName,
            LocalDateTime startDate,
            LocalDateTime endDate,
            VacationType type,
            String reason,
            ApprovalStatus approvalStatus,
            String approverNote,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    public record VacationApprovalRequest(
            ApprovalStatus approvalStatus,
            String approverNote
    ) {}
}