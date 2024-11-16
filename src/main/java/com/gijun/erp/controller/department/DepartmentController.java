package com.gijun.erp.controller.department;

import com.gijun.erp.common.response.ApiResponse;
import com.gijun.erp.dto.department.DepartmentDto;
import com.gijun.erp.service.department.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Department", description = "부서 관리 API")
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(summary = "부서 생성", description = "새로운 부서를 생성합니다.")
    @PostMapping
    public ApiResponse<DepartmentDto.Response> createDepartment(
            @Valid @RequestBody DepartmentDto.CreateRequest request) {
        return ApiResponse.success(departmentService.createDepartment(request));
    }

    @Operation(summary = "부서 목록 조회", description = "전체 부서 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<List<DepartmentDto.Response>> getAllDepartments() {
        return ApiResponse.success(departmentService.getAllDepartments());
    }

    @Operation(summary = "부서 상세 조회", description = "부서 ID로 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ApiResponse<DepartmentDto.Response> getDepartment(@PathVariable Long id) {
        return ApiResponse.success(departmentService.getDepartment(id));
    }

    @Operation(summary = "부서 정보 수정", description = "부서 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ApiResponse<DepartmentDto.Response> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentDto.UpdateRequest request) {
        return ApiResponse.success(departmentService.updateDepartment(id, request));
    }

    @Operation(summary = "부서 삭제", description = "부서를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ApiResponse.successResponse();
    }
}