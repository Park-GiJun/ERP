package com.gijun.erp.dto.position;

import com.gijun.erp.domain.position.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class PositionDto {

    @Schema(description = "직급 생성 요청")
    public record CreateRequest(
            @Schema(description = "직급명", example = "사원")
            @NotBlank(message = "직급명은 필수입니다")
            @Size(min = 2, max = 50, message = "직급명은 2~50자 사이여야 합니다")
            String name,

            @Schema(description = "직급 코드", example = "P001")
            @NotBlank(message = "직급 코드는 필수입니다")
            @Pattern(regexp = "^[A-Z][0-9]{3}$", message = "직급 코드는 영대문자 1자와 숫자 3자로 구성되어야 합니다")
            String code,

            @Schema(description = "직급 레벨", example = "1")
            @Positive(message = "직급 레벨은 양수여야 합니다")
            int level,

            @Schema(description = "정렬 순서", example = "1")
            int sortOrder,

            @Schema(description = "직급 설명", example = "일반 사원")
            @Size(max = 255, message = "설명은 255자를 초과할 수 없습니다")
            String description
    ) {}

    @Schema(description = "직급 수정 요청")
    public record UpdateRequest(
            @Schema(description = "직급명", example = "사원")
            @NotBlank(message = "직급명은 필수입니다")
            @Size(min = 2, max = 50, message = "직급명은 2~50자 사이여야 합니다")
            String name,

            @Schema(description = "직급 코드", example = "P001")
            @NotBlank(message = "직급 코드는 필수입니다")
            @Pattern(regexp = "^[A-Z][0-9]{3}$", message = "직급 코드는 영대문자 1자와 숫자 3자로 구성되어야 합니다")
            String code,

            @Schema(description = "직급 레벨", example = "1")
            @Positive(message = "직급 레벨은 양수여야 합니다")
            int level,

            @Schema(description = "정렬 순서", example = "1")
            int sortOrder,

            @Schema(description = "직급 설명", example = "일반 사원")
            @Size(max = 255, message = "설명은 255자를 초과할 수 없습니다")
            String description
    ) {}

    @Schema(description = "직급 응답")
    public record Response(
            @Schema(description = "직급 ID")
            Long id,

            @Schema(description = "직급명")
            String name,

            @Schema(description = "직급 코드")
            String code,

            @Schema(description = "직급 레벨")
            int level,

            @Schema(description = "정렬 순서")
            int sortOrder,

            @Schema(description = "직급 설명")
            String description,

            @Schema(description = "생성 일시")
            LocalDateTime createdAt,

            @Schema(description = "수정 일시")
            LocalDateTime updatedAt
    ) {
        public static Response from(Position position) {
            return new Response(
                    position.getId(),
                    position.getName(),
                    position.getCode(),
                    position.getLevel(),
                    position.getSortOrder(),
                    position.getDescription(),
                    position.getCreatedAt(),
                    position.getUpdatedAt()
            );
        }
    }

    @Schema(description = "직급 검색 조건")
    public record SearchCondition(
            @Schema(description = "직급명")
            String name,

            @Schema(description = "직급 코드")
            String code,

            @Schema(description = "직급 레벨")
            Integer level
    ) {}
}