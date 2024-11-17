// src/types/api.ts
export type ApiResponse<T> = {
    success: boolean;
    data: T;
    error?: {
        code: string;
        message: string;
        detail?: string;
    };
    timestamp: string;
};

export type User = {
    id: number;
    email: string;
    name: string;
    employeeNumber: string;
    phoneNumber?: string;
    role: string;
    departmentId?: number;
    positionId?: number;
    createdAt: string;
    updatedAt: string;
};

export type Vacation = {
    id: number;
    userId: number;
    userName: string;
    startDate: string;
    endDate: string;
    type: string;
    reason: string;
    approvalStatus: string;
    approverNote?: string;
    createdAt: string;
    updatedAt: string;
};

export type Department = {
    id: number;
    name: string;
    code: string;
    parentId?: number;
    sortOrder: number;
    description?: string;
    createdAt: string;
    updatedAt: string;
};

export type Position = {
    id: number;
    name: string;
    code: string;
    level: number;
    sortOrder: number;
    description?: string;
    createdAt: string;
    updatedAt: string;
};