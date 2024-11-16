package com.gijun.erp.dto.common;

import java.time.LocalDateTime;

public record SearchCondition(
        LocalDateTime startDate,
        LocalDateTime endDate,
        String searchKey,
        String searchValue,
        String sortBy,
        String sortDirection
) {}