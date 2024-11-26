// src/types/attendance.ts

export enum AttendanceStatus {
    CHECKED_IN = 'CHECKED_IN',
    CHECKED_OUT = 'CHECKED_OUT',
    LATE = 'LATE',
    ABSENT = 'ABSENT',
    VACATION = 'VACATION'
}

export interface AttendanceRecord {
    id: number;
    userId: number;
    userName: string;
    workDate: string;
    checkIn: string;
    checkOut: string;
    status: AttendanceStatus;
    note?: string;
    createdAt: string;
    updatedAt: string;
}

export interface AttendanceSearchParams {
    startDate: string;
    endDate: string;
    userId?: number;
    page?: number;
    size?: number;
}

export interface AttendanceSummary {
    total: number;
    checkedOut: number;
    late: number;
    absent: number;
    vacation: number;
}

export interface AttendancePage {
    content: AttendanceRecord[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
}