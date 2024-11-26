import React, { useState } from 'react';
import axios from '@/lib/axios';
import { useQuery } from '@tanstack/react-query';
import dayjs from 'dayjs';
import { Calendar, Clock, FileDown } from 'lucide-react';
import {
    AttendanceRecord,
    AttendanceStatus,
    AttendanceSearchParams,
    AttendancePage,
    AttendanceSummary
} from '@/types/attendance';
import { ApiResponse } from '@/types/api';

const AttendanceStatusPage = () => {
    const [searchParams, setSearchParams] = useState<AttendanceSearchParams>({
        startDate: dayjs().startOf('month').format('YYYY-MM-DD'),
        endDate: dayjs().format('YYYY-MM-DD'),
        page: 0,
        size: 100
    });

    const { data: response, isLoading } = useQuery<ApiResponse<AttendancePage>>({
        queryKey: ['attendance', searchParams],
        queryFn: async () => {
            const response = await axios.get<ApiResponse<AttendancePage>>('/api/attendance/search', {
                params: {
                    startDate: `${searchParams.startDate}T00:00:00`,
                    endDate: `${searchParams.endDate}T23:59:59`,
                    page: searchParams.page,
                    size: searchParams.size
                }
            });
            return response.data;
        }
    });

    const attendanceRecords = response?.data?.content || [];

    const calculateSummary = (records: AttendanceRecord[]): AttendanceSummary => {
        return {
            total: records.length,
            checkedOut: records.filter(r => r.status === AttendanceStatus.CHECKED_OUT).length,
            late: records.filter(r => r.status === AttendanceStatus.LATE).length,
            absent: records.filter(r => r.status === AttendanceStatus.ABSENT).length,
            vacation: records.filter(r => r.status === AttendanceStatus.VACATION).length
        };
    };

    const summary = calculateSummary(attendanceRecords);

    const getStatusColor = (status: AttendanceStatus): string => {
        switch (status) {
            case AttendanceStatus.CHECKED_OUT:
                return 'bg-green-100 text-green-800';
            case AttendanceStatus.CHECKED_IN:
                return 'bg-blue-100 text-blue-800';
            case AttendanceStatus.LATE:
                return 'bg-yellow-100 text-yellow-800';
            case AttendanceStatus.ABSENT:
                return 'bg-red-100 text-red-800';
            case AttendanceStatus.VACATION:
                return 'bg-purple-100 text-purple-800';
        }
    };

    const getStatusText = (status: AttendanceStatus): string => {
        switch (status) {
            case AttendanceStatus.CHECKED_OUT:
                return '퇴근';
            case AttendanceStatus.CHECKED_IN:
                return '출근';
            case AttendanceStatus.LATE:
                return '지각';
            case AttendanceStatus.ABSENT:
                return '결근';
            case AttendanceStatus.VACATION:
                return '휴가';
        }
    };

    const handleDateChange = (field: keyof Pick<AttendanceSearchParams, 'startDate' | 'endDate'>) => (
        e: React.ChangeEvent<HTMLInputElement>
    ) => {
        setSearchParams(prev => ({ ...prev, [field]: e.target.value }));
    };

    const exportToExcel = () => {
        alert('Excel 다운로드 기능은 추후 구현 예정입니다.');
    };

    if (isLoading) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
            </div>
        );
    }

    return (
        <div className="p-8 space-y-8">
            {/* 타이틀 및 액션 버튼 */}
            <div className="flex justify-between items-center">
                <h1 className="text-2xl font-bold text-gray-900">근태 현황</h1>
                <button
                    onClick={exportToExcel}
                    className="inline-flex items-center px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition shadow"
                >
                    <FileDown className="w-5 h-5 mr-2" />
                    Excel 다운로드
                </button>
            </div>

            {/* 검색 필터 */}
            <div className="bg-white p-6 rounded-lg shadow-md space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">시작 날짜</label>
                        <input
                            type="date"
                            value={searchParams.startDate}
                            onChange={handleDateChange('startDate')}
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">종료 날짜</label>
                        <input
                            type="date"
                            value={searchParams.endDate}
                            onChange={handleDateChange('endDate')}
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                        />
                    </div>
                </div>
            </div>

            {/* 통계 카드 */}
            <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
                <div className="bg-white p-6 rounded-lg shadow-md">
                    <div className="flex items-center">
                        <div className="p-2 bg-blue-100 rounded-lg">
                            <Clock className="w-6 h-6 text-blue-600" />
                        </div>
                        <div className="ml-4">
                            <p className="text-sm text-gray-500">정상 출근</p>
                            <p className="text-xl font-semibold text-gray-900">
                                {summary.checkedOut}명
                            </p>
                        </div>
                    </div>
                </div>

                <div className="bg-white p-6 rounded-lg shadow-md">
                    <div className="flex items-center">
                        <div className="p-2 bg-yellow-100 rounded-lg">
                            <Clock className="w-6 h-6 text-yellow-600" />
                        </div>
                        <div className="ml-4">
                            <p className="text-sm text-gray-500">지각</p>
                            <p className="text-xl font-semibold text-gray-900">
                                {summary.late}명
                            </p>
                        </div>
                    </div>
                </div>

                <div className="bg-white p-6 rounded-lg shadow-md">
                    <div className="flex items-center">
                        <div className="p-2 bg-red-100 rounded-lg">
                            <Calendar className="w-6 h-6 text-red-600" />
                        </div>
                        <div className="ml-4">
                            <p className="text-sm text-gray-500">결근</p>
                            <p className="text-xl font-semibold text-gray-900">
                                {summary.absent}명
                            </p>
                        </div>
                    </div>
                </div>

                <div className="bg-white p-6 rounded-lg shadow-md">
                    <div className="flex items-center">
                        <div className="p-2 bg-purple-100 rounded-lg">
                            <Calendar className="w-6 h-6 text-purple-600" />
                        </div>
                        <div className="ml-4">
                            <p className="text-sm text-gray-500">휴가</p>
                            <p className="text-xl font-semibold text-gray-900">
                                {summary.vacation}명
                            </p>
                        </div>
                    </div>
                </div>
            </div>

            {/* 근태 기록 테이블 */}
            <div className="bg-white rounded-lg shadow-md overflow-hidden">
                <div className="overflow-x-auto">
                    <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">날짜</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">사원명</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">출근 시각</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">퇴근 시각</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">상태</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">비고</th>
                        </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-200">
                        {attendanceRecords.map((record) => (
                            <tr key={record.id} className="hover:bg-gray-50">
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {dayjs(record.workDate).format('YYYY-MM-DD')}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {record.userName}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {record.checkIn ? dayjs(record.checkIn).format('HH:mm:ss') : '-'}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {record.checkOut ? dayjs(record.checkOut).format('HH:mm:ss') : '-'}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                        <span className={`px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${getStatusColor(record.status)}`}>
                                            {getStatusText(record.status)}
                                        </span>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {record.note || '-'}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>

                {attendanceRecords.length === 0 && (
                    <div className="text-center py-8 text-gray-500">
                        조회된 근태 기록이 없습니다.
                    </div>
                )}
            </div>
        </div>
    );
};

export default AttendanceStatusPage;