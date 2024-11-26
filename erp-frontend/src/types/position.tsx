// src/types/position.ts
export interface Position {
    id: number;
    name: string;
    code: string;
    level: number;
    sortOrder: number;
    description?: string;
    createdAt: string;
    updatedAt: string;
}

export interface PositionCreateRequest {
    name: string;
    code: string;
    level: number;
    sortOrder: number;
    description?: string;
}

export interface PositionUpdateRequest {
    name: string;
    code: string;
    level: number;
    sortOrder: number;
    description?: string;
}