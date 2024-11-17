package com.gijun.erp.controller.attendance;

import com.gijun.erp.common.exception.BaseException;
import com.gijun.erp.common.exception.ErrorCode;
import com.gijun.erp.common.response.ApiResponse;
import com.gijun.erp.dto.attendance.AttendanceDto.*;
import com.gijun.erp.security.UserSecurityAdapter;
import com.gijun.erp.service.attendance.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "근태관리 API", description = "근태관리 관련 API")
@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @Operation(summary = "출근 체크인")
    @PostMapping("/check-in")
    public ApiResponse<AttendanceResponse> checkIn(
            @AuthenticationPrincipal UserSecurityAdapter userAdapter,
            @RequestBody CheckInRequest request
    ) {
        if (userAdapter == null) {
            throw new BaseException(ErrorCode.UNAUTHORIZED, "인증 정보가 없습니다.");
        }
        return ApiResponse.success(
                attendanceService.checkIn(userAdapter.getUser().getId(), request)
        );
    }

    @Operation(summary = "퇴근 체크아웃")
    @PostMapping("/check-out")
    public ApiResponse<AttendanceResponse> checkOut(
            @AuthenticationPrincipal UserSecurityAdapter userAdapter,
            @RequestBody CheckOutRequest request
    ) {

        if (userAdapter == null) {
            throw new BaseException(ErrorCode.UNAUTHORIZED, "인증 정보가 없습니다.");
        }
        return ApiResponse.success(
                attendanceService.checkOut(userAdapter.getUser().getId(), request)
        );
    }

    @Operation(summary = "근태 기록 조회")
    @GetMapping
    public ApiResponse<Page<AttendanceResponse>> searchAttendances(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @PageableDefault(size = 10, sort = "workDate", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        if (startDate == null) {
            startDate = LocalDateTime.now().minusMonths(1);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }

        return ApiResponse.success(
                attendanceService.searchAttendances(startDate, endDate, pageable)
        );
    }
}