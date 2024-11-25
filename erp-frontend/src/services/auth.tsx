// src/services/auth.ts
import axios from '@/lib/axios';
import type { LoginRequest, LoginResponse, ApiResponse } from '@/types/api';

export const authService = {
    // /api/public/auth/login 경로로 수정
    login: async (data: LoginRequest): Promise<ApiResponse<LoginResponse>> => {
        return axios.post('/public/auth/login', data);
    },

    // /api/public/auth/refresh 경로로 수정
    refresh: async (refreshToken: string): Promise<ApiResponse<{ accessToken: string }>> => {
        return axios.post('/public/auth/refresh', { refreshToken });
    }
};