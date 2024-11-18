// src/api/services.ts
import axiosInstance from './axiosInstance';
import { ApiResponse, User, Vacation } from '../types/api';

// Attendance/Vacation Service
interface VacationListResponse {
    content: Vacation[];
    pageInfo: {
        pageNumber: number;
        pageSize: number;
        totalElements: number;
        totalPages: number;
    };
}

export const attendanceService = {
    // 휴가 신청
    requestVacation: (data: {
        startDate: string;
        endDate: string;
        type: string;
        reason: string;
    }): Promise<ApiResponse<Vacation>> =>
        axiosInstance.post('/api/vacations', data),

    // 휴가 목록 조회
    getVacations: (params: {
        page?: number;
        size?: number;
    }): Promise<ApiResponse<VacationListResponse>> =>
        axiosInstance.get('/api/vacations', { params }),

    // 휴가 상세 조회
    getVacationDetail: (id: number): Promise<ApiResponse<Vacation>> =>
        axiosInstance.get(`/api/vacations/${id}`),

    // 휴가 승인/거절
    updateVacationStatus: (id: number, data: {
        approvalStatus: string;
        approverNote?: string;
    }): Promise<ApiResponse<Vacation>> =>
        axiosInstance.patch(`/api/vacations/${id}/approval`, data),

    // 휴가 취소
    deleteVacation: (id: number): Promise<ApiResponse<void>> =>
        axiosInstance.delete(`/api/vacations/${id}`),
};

// User Service
export const userService = {
    getProfile: (): Promise<ApiResponse<User>> =>
        axiosInstance.get('/api/users/me'),

    updateProfile: (data: {
        name?: string;
        phoneNumber?: string;
    }): Promise<ApiResponse<User>> =>
        axiosInstance.put('/api/users/me', data),

    changePassword: (data: {
        currentPassword: string;
        newPassword: string;
    }): Promise<ApiResponse<void>> =>
        axiosInstance.put('/api/users/me/password', data),
};

// Auth Service
interface LoginResponse {
    accessToken: string;
    refreshToken: string;
    userInfo: User;
}

export const authService = {
    login: (data: {
        email: string;
        password: string;
    }): Promise<ApiResponse<LoginResponse>> =>
        axiosInstance.post('/api/public/auth/login', data),

    register: (data: {
        email: string;
        password: string;
        name: string;
        employeeNumber: string;
        phoneNumber: string;
    }): Promise<ApiResponse<User>> =>
        axiosInstance.post('/auth/register', data),

    logout: (): Promise<ApiResponse<void>> =>
        axiosInstance.post('/auth/logout'),

    refreshToken: (refreshToken: string): Promise<ApiResponse<{ accessToken: string }>> =>
        axiosInstance.post('/auth/refresh', { refreshToken }),
};

// Department Service
interface DepartmentCreateRequest {
    name: string;
    code: string;
    parentId?: number;
    description?: string;
}

interface Department {
    id: number;
    name: string;
    code: string;
    parentId?: number;
    children?: Department[];
    description?: string;
    createdAt: string;
    updatedAt: string;
}

export const departmentService = {
    getDepartments: (): Promise<ApiResponse<Department[]>> =>
        axiosInstance.get('/departments'),

    getDepartmentTree: (): Promise<ApiResponse<Department[]>> =>
        axiosInstance.get('/departments/tree'),

    createDepartment: (data: DepartmentCreateRequest): Promise<ApiResponse<Department>> =>
        axiosInstance.post('/departments', data),

    updateDepartment: (
        id: number,
        data: Partial<DepartmentCreateRequest>
    ): Promise<ApiResponse<Department>> =>
        axiosInstance.put(`/departments/${id}`, data),

    deleteDepartment: (id: number): Promise<ApiResponse<void>> =>
        axiosInstance.delete(`/departments/${id}`),
};

// Position Service
interface Position {
    id: number;
    name: string;
    code: string;
    level: number;
    description?: string;
    createdAt: string;
    updatedAt: string;
}

interface PositionCreateRequest {
    name: string;
    code: string;
    level: number;
    description?: string;
}

export const positionService = {
    getPositions: (): Promise<ApiResponse<Position[]>> =>
        axiosInstance.get('/positions'),

    createPosition: (data: PositionCreateRequest): Promise<ApiResponse<Position>> =>
        axiosInstance.post('/positions', data),

    updatePosition: (
        id: number,
        data: Partial<PositionCreateRequest>
    ): Promise<ApiResponse<Position>> =>
        axiosInstance.put(`/positions/${id}`, data),

    deletePosition: (id: number): Promise<ApiResponse<void>> =>
        axiosInstance.delete(`/positions/${id}`),
};