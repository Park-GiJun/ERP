// src/types/vacation.ts

export enum VacationType {
    ANNUAL = 'ANNUAL',
    HALF_DAY_AM = 'HALF_DAY_AM',
    HALF_DAY_PM = 'HALF_DAY_PM',
    SICK = 'SICK',
    SPECIAL = 'SPECIAL'
}

export enum ApprovalStatus {
    PENDING = 'PENDING',
    APPROVED = 'APPROVED',
    REJECTED = 'REJECTED',
    CANCELED = 'CANCELED'
}

export interface VacationRequest {
    startDate: string;
    endDate: string;
    type: VacationType;
    reason: string;
}

export interface VacationResponse {
    id: number;
    userId: number;
    userName: string;
    startDate: string;
    endDate: string;
    type: VacationType;
    reason: string;
    approvalStatus: ApprovalStatus;
    approverNote?: string;
    createdAt: string;
    updatedAt: string;
}

export interface VacationBalance {
    total: number;
    used: number;
    remaining: number;
    year: number;
}

export interface VacationSearchParams {
    userId?: number;
    startDate?: string;
    endDate?: string;
    type?: VacationType;
    status?: ApprovalStatus;
    page?: number;
    size?: number;
}

export interface VacationPage {
    content: VacationResponse[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
}