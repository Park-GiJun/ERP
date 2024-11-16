package com.gijun.erp.dto.common;

import java.util.List;

public class PageDto {
    public record PageInfo(
            int pageNumber,
            int pageSize,
            long totalElements,
            int totalPages
    ) {}

    public record PageResponse<T>(
            List<T> content,
            PageInfo pageInfo
    ) {}
}