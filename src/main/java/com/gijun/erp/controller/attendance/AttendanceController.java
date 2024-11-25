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

@Tag(name = "근태 관리", description = "근태 관리 API")
@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @Operation(summary = "출근 체크인")
    @PostMapping("/check-in")
    public ApiResponse<AttendanceResponse> checkIn(
            @RequestParam Long userId,
            @RequestBody CheckInRequest request) {
        return ApiResponse.success(attendanceService.checkIn(userId, request));
    }

    @Operation(summary = "퇴근 체크아웃")
    @PostMapping("/check-out")
    public ApiResponse<AttendanceResponse> checkOut(
            @RequestParam Long userId,
            @RequestBody CheckOutRequest request) {
        return ApiResponse.success(attendanceService.checkOut(userId, request));
    }

    @Operation(summary = "근태 목록 조회")
    @GetMapping("/search")
    public ApiResponse<Page<AttendanceResponse>> searchAttendances(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        return ApiResponse.success(attendanceService.searchAttendances(startDate, endDate, pageable));
    }

    @Operation(summary = "개인 근태 목록 조회")
    @GetMapping("/search/{userId}")
    public ApiResponse<Page<AttendanceResponse>> searchAttendancesSolo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @PathVariable Long userId,
            Pageable pageable) {
        return ApiResponse.success(attendanceService.searchAttendancesSolo(startDate, endDate, userId ,pageable));
    }
}
