package com.gijun.erp.controller.attendance;

import com.gijun.erp.common.response.ApiResponse;
import com.gijun.erp.dto.attendance.AttendanceVacationDto.VacationRequest;
import com.gijun.erp.dto.attendance.AttendanceVacationDto.VacationResponse;
import com.gijun.erp.dto.attendance.AttendanceVacationDto.VacationApprovalRequest;
import com.gijun.erp.service.attendance.VacationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "휴가 관리", description = "휴가 신청 및 관리 API")
@RestController
@RequestMapping("/api/vacations")
@RequiredArgsConstructor
public class VacationController {

    private final VacationService vacationService;

    @Operation(summary = "휴가 신청")
    @PostMapping
    public ApiResponse<VacationResponse> createVacation(
            @RequestParam Long userId,
            @RequestBody VacationRequest request) {
        return ApiResponse.success(vacationService.createVacation(userId, request));
    }

    @Operation(summary = "휴가 상세 조회")
    @GetMapping("/{vacationId}")
    public ApiResponse<VacationResponse> getVacation(@PathVariable Long vacationId) {
        return ApiResponse.success(vacationService.getVacation(vacationId));
    }

    @Operation(summary = "사용자별 휴가 목록 조회")
    @GetMapping
    public ApiResponse<Page<VacationResponse>> getVacationList(
            @RequestParam Long userId,
            Pageable pageable) {
        return ApiResponse.success(vacationService.getVacationList(userId, pageable));
    }

    @Operation(summary = "휴가 승인/거절")
    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping("/{vacationId}/approval")
    public ApiResponse<VacationResponse> updateVacationApproval(
            @PathVariable Long vacationId,
            @RequestBody VacationApprovalRequest request) {
        return ApiResponse.success(vacationService.updateVacationApproval(vacationId, request));
    }

    @Operation(summary = "휴가 신청 취소")
    @DeleteMapping("/{vacationId}")
    public ApiResponse<Void> deleteVacation(@PathVariable Long vacationId) {
        vacationService.deleteVacation(vacationId);
        return ApiResponse.successResponse();
    }
}
