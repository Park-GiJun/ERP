// src/services/auth.ts
import axios from '@/lib/axios';
import type { LoginRequest, LoginResponse, ApiResponse } from '@/types/api';

export const authService = {
    login: async (data: LoginRequest): Promise<ApiResponse<LoginResponse>> => {
        return axios.post('/public/auth/login', data);
    },

    refresh: async (refreshToken: string): Promise<ApiResponse<{ accessToken: string }>> => {
        return axios.post('/public/auth/refresh', { refreshToken });
    }
};
