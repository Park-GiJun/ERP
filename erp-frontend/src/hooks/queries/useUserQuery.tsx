// src/hooks/queries/useUserQuery.ts
import { useQuery } from '@tanstack/react-query';
import { userService } from '../../api/services';

export const useUserProfile = () => {
    return useQuery({
        queryKey: ['userProfile'],
        queryFn: () => userService.getProfile(),
        staleTime: 5 * 60 * 1000,
    });
};