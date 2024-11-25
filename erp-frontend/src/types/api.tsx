// src/types/api.ts
export interface ApiResponse<T> {
    success: boolean;
    data: T;
    error?: {
        code: string;
        message: string;
        detail?: string;
    };
    timestamp: string;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface UserInfo {
    id: number;
    email: string;
    name: string;
    role: string;
    status: string;
    departmentId?: number;
    positionId?: number;
}

export interface LoginResponse {
    accessToken: string;
    refreshToken: string;
    userInfo: UserInfo;
}