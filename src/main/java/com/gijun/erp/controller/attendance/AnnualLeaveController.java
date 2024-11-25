package com.gijun.erp.controller.attendance;

import com.gijun.erp.common.response.ApiResponse;
import com.gijun.erp.dto.attendance.AnnualLeaveDto.*;
import com.gijun.erp.service.attendance.AnnualLeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "연차 관리", description = "연차 발생 및 관리 API")
@RestController
@RequestMapping("/api/annual-leaves")
@RequiredArgsConstructor
public class AnnualLeaveController {

    private final AnnualLeaveService annualLeaveService;

    @Operation(summary = "연차 발생")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Response> createAnnualLeave(
            @Valid @RequestBody CreateRequest request) {
        return ApiResponse.success(annualLeaveService.createAnnualLeave(request));
    }

    @Operation(summary = "연차 현황 조회")
    @GetMapping("/{userId}")
    public ApiResponse<Response> getAnnualLeave(
            @PathVariable Long userId,
            @RequestParam Integer year) {
        return ApiResponse.success(annualLeaveService.getAnnualLeave(userId, year));
    }
}