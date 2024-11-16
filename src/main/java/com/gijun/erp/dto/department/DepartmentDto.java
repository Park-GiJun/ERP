package com.gijun.erp.dto.department;

import com.gijun.erp.domain.department.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class DepartmentDto {

    @Schema(description = "부서 생성 요청")
    public record CreateRequest(
            @Schema(description = "부서명", example = "개발팀")
            @NotBlank(message = "부서명은 필수입니다")
            @Size(min = 2, max = 50, message = "부서명은 2~50자 사이여야 합니다")
            String name,

            @Schema(description = "부서 코드", example = "DEV")
            @NotBlank(message = "부서 코드는 필수입니다")
            @Pattern(regexp = "^[A-Z0-9]{2,20}$", message = "부서 코드는 2~20자의 영대문자와 숫자만 가능합니다")
            String code,

            @Schema(description = "상위 부서 ID", example = "1")
            Long parentId,

            @Schema(description = "정렬 순서", example = "1")
            int sortOrder,

            @Schema(description = "부서 설명", example = "소프트웨어 개발팀")
            @Size(max = 255, message = "설명은 255자를 초과할 수 없습니다")
            String description
    ) {}

    @Schema(description = "부서 수정 요청")
    public record UpdateRequest(
            @Schema(description = "부서명", example = "개발팀")
            @NotBlank(message = "부서명은 필수입니다")
            @Size(min = 2, max = 50, message = "부서명은 2~50자 사이여야 합니다")
            String name,

            @Schema(description = "부서 코드", example = "DEV")
            @NotBlank(message = "부서 코드는 필수입니다")
            @Pattern(regexp = "^[A-Z0-9]{2,20}$", message = "부서 코드는 2~20자의 영대문자와 숫자만 가능합니다")
            String code,

            @Schema(description = "상위 부서 ID", example = "1")
            Long parentId,

            @Schema(description = "정렬 순서", example = "1")
            int sortOrder,

            @Schema(description = "부서 설명", example = "소프트웨어 개발팀")
            @Size(max = 255, message = "설명은 255자를 초과할 수 없습니다")
            String description
    ) {}

    @Schema(description = "부서 응답")
    public record Response(
            @Schema(description = "부서 ID")
            Long id,

            @Schema(description = "부서명")
            String name,

            @Schema(description = "부서 코드")
            String code,

            @Schema(description = "상위 부서 ID")
            Long parentId,

            @Schema(description = "정렬 순서")
            int sortOrder,

            @Schema(description = "부서 설명")
            String description,

            @Schema(description = "생성 일시")
            LocalDateTime createdAt,

            @Schema(description = "수정 일시")
            LocalDateTime updatedAt
    ) {
        public static Response from(Department department) {
            return new Response(
                    department.getId(),
                    department.getName(),
                    department.getCode(),
                    department.getParentId(),
                    department.getSortOrder(),
                    department.getDescription(),
                    department.getCreatedAt(),
                    department.getUpdatedAt()
            );
        }
    }

    @Schema(description = "부서 검색 조건")
    public record SearchCondition(
            @Schema(description = "부서명")
            String name,

            @Schema(description = "부서 코드")
            String code,

            @Schema(description = "상위 부서 ID")
            Long parentId
    ) {}
}