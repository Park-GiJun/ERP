package com.gijun.erp.controller.position;

import com.gijun.erp.common.response.ApiResponse;
import com.gijun.erp.dto.position.PositionDto;
import com.gijun.erp.service.position.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Position", description = "직급 관리 API")
@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @Operation(summary = "직급 생성", description = "새로운 직급을 생성합니다.")
    @PostMapping
    public ApiResponse<PositionDto.Response> createPosition(
            @Valid @RequestBody PositionDto.CreateRequest request) {
        return ApiResponse.success(positionService.createPosition(request));
    }

    @Operation(summary = "직급 목록 조회", description = "전체 직급 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<List<PositionDto.Response>> getAllPositions() {
        return ApiResponse.success(positionService.getAllPositions());
    }

    @Operation(summary = "직급 상세 조회", description = "직급 ID로 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ApiResponse<PositionDto.Response> getPosition(@PathVariable Long id) {
        return ApiResponse.success(positionService.getPosition(id));
    }

    @Operation(summary = "직급 정보 수정", description = "직급 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ApiResponse<PositionDto.Response> updatePosition(
            @PathVariable Long id,
            @Valid @RequestBody PositionDto.UpdateRequest request) {
        return ApiResponse.success(positionService.updatePosition(id, request));
    }

    @Operation(summary = "직급 삭제", description = "직급을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePosition(@PathVariable Long id) {
        positionService.deletePosition(id);
        return ApiResponse.successResponse();
    }
}