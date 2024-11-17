import { useQuery } from '@tanstack/react-query';
import { attendanceService } from '../../api/services';
import type { ApiResponse, Vacation } from '../../types/api';

interface VacationResponse {
    content: Vacation[];
    pageInfo: {
        pageNumber: number;
        pageSize: number;
        totalElements: number;
        totalPages: number;
    };
}

export const useVacationList = (page = 0, size = 5) => {
    return useQuery({
        queryKey: ['vacations', page, size],
        queryFn: () => attendanceService.getVacations({ page, size }),
        placeholderData: (previousData) => previousData
    });
};