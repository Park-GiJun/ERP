// src/types/department.ts
export interface Department {
    id: number;
    name: string;
    code: string;
    parentId?: number;
    sortOrder: number;
    description?: string;
    createdAt: string;
    updatedAt: string;
}

export interface DepartmentCreateRequest {
    name: string;
    code: string;
    parentId?: number;
    sortOrder: number;
    description?: string;
}

export interface DepartmentUpdateRequest {
    name: string;
    code: string;
    parentId?: number;
    sortOrder: number;
    description?: string;
}