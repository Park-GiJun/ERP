// src/pages/attendance/VacationApprovalPage.tsx
// @ts-ignore
import React, {useState} from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import axios from '@/lib/axios';
import dayjs from 'dayjs';
import { Check, X } from 'lucide-react';
import {
    VacationType,
    VacationResponse,
    ApprovalStatus,
    VacationSearchParams,
    VacationPage
} from '@/types/vacation';
import { ApiResponse } from '@/types/api';

const VacationApprovalPage = () => {
    const queryClient = useQueryClient();
    const [searchParams, setSearchParams] = useState<VacationSearchParams>({
        startDate: dayjs().subtract(1, 'month').format('YYYY-MM-DD'),
        endDate: dayjs().format('YYYY-MM-DD'),
        status: ApprovalStatus.PENDING,
        page: 0,
        size: 10
    });

    const [selectedVacation, setSelectedVacation] = useState<VacationResponse | null>(null);
    const [approvalNote, setApprovalNote] = useState<string>('');
    const [isModalOpen, setIsModalOpen] = useState(false);

    const { data: vacations, isLoading } = useQuery<ApiResponse<VacationPage>>({
        queryKey: ['vacation-approvals', searchParams],
        queryFn: async () => {
            const response = await axios.get('/api/vacations', {
                params: searchParams
            });
            return response.data;
        }
    });

    const approvalMutation = useMutation({
        mutationFn: async (params: {
            vacationId: number;
            status: ApprovalStatus;
            note: string;
        }) => {
            return axios.patch(`/api/vacations/${params.vacationId}/approval`, {
                approvalStatus: params.status,
                approverNote: params.note
            });
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['vacation-approvals'] });
            setIsModalOpen(false);
            setSelectedVacation(null);
            setApprovalNote('');
        }
    });

    const handleOpenModal = (vacation: VacationResponse) => {
        setSelectedVacation(vacation);
        setIsModalOpen(true);
    };

    const getVacationTypeText = (type: VacationType): string => {
        switch (type) {
            case VacationType.ANNUAL:
                return '연차';
            case VacationType.HALF_DAY_AM:
                return '오전 반차';
            case VacationType.HALF_DAY_PM:
                return '오후 반차';
            case VacationType.SICK:
                return '병가';
            case VacationType.SPECIAL:
                return '특별휴가';
        }
    };

    const getStatusColor = (status: ApprovalStatus): string => {
        switch (status) {
            case ApprovalStatus.APPROVED:
                return 'bg-green-100 text-green-800';
            case ApprovalStatus.PENDING:
                return 'bg-yellow-100 text-yellow-800';
            case ApprovalStatus.REJECTED:
                return 'bg-red-100 text-red-800';
            case ApprovalStatus.CANCELED:
                return 'bg-gray-100 text-gray-800';
        }
    };

    const getStatusText = (status: ApprovalStatus): string => {
        switch (status) {
            case ApprovalStatus.APPROVED:
                return '승인';
            case ApprovalStatus.PENDING:
                return '대기';
            case ApprovalStatus.REJECTED:
                return '반려';
            case ApprovalStatus.CANCELED:
                return '취소';
        }
    };

    const confirmApproval = async (status: ApprovalStatus) => {
        if (!selectedVacation) return;

        try {
            await approvalMutation.mutateAsync({
                vacationId: selectedVacation.id,
                status,
                note: approvalNote
            });
            alert(status === ApprovalStatus.APPROVED ? '승인되었습니다.' : '반려되었습니다.');
        } catch (error) {
            alert('처리 중 오류가 발생했습니다.');
        }
    };

    if (isLoading) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
            </div>
        );
    }

    return (
        <div className="p-8 max-w-7xl mx-auto space-y-8">
            <h1 className="text-2xl font-bold text-gray-900">휴가 승인</h1>

            {/* 검색 필터 */}
            <div className="bg-white rounded-lg shadow p-6 space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">시작일</label>
                        <input
                            type="date"
                            value={searchParams.startDate}
                            onChange={(e) => setSearchParams(prev => ({ ...prev, startDate: e.target.value }))}
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">종료일</label>
                        <input
                            type="date"
                            value={searchParams.endDate}
                            onChange={(e) => setSearchParams(prev => ({ ...prev, endDate: e.target.value }))}
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">상태</label>
                        <select
                            value={searchParams.status}
                            onChange={(e) => setSearchParams(prev => ({
                                ...prev,
                                status: e.target.value as ApprovalStatus
                            }))}
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                        >
                            <option value={ApprovalStatus.PENDING}>대기</option>
                            <option value={ApprovalStatus.APPROVED}>승인</option>
                            <option value={ApprovalStatus.REJECTED}>반려</option>
                        </select>
                    </div>
                </div>
            </div>

            {/* 휴가 신청 목록 */}
            <div className="bg-white rounded-lg shadow overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">신청일</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">사원명</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">휴가 종류</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">휴가 기간</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">사유</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">상태</th>
                        <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase">작업</th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                    {vacations?.data?.content?.map((vacation) => (
                        <tr key={vacation.id} className="hover:bg-gray-50">
                            <td className="px-6 py-4 text-sm text-gray-900">
                                {dayjs(vacation.createdAt).format('YYYY-MM-DD')}
                            </td>
                            <td className="px-6 py-4 text-sm text-gray-900">{vacation.userName}</td>
                            <td className="px-6 py-4 text-sm text-gray-900">
                                {getVacationTypeText(vacation.type)}
                            </td>
                            <td className="px-6 py-4 text-sm text-gray-900">
                                {dayjs(vacation.startDate).format('YYYY-MM-DD')} ~
                                {dayjs(vacation.endDate).format('YYYY-MM-DD')}
                            </td>
                            <td className="px-6 py-4 text-sm text-gray-900">{vacation.reason}</td>
                            <td className="px-6 py-4">
                                    <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full
                                        ${getStatusColor(vacation.approvalStatus)}`}>
                                        {getStatusText(vacation.approvalStatus)}
                                    </span>
                            </td>
                            <td className="px-6 py-4 text-right">
                                {vacation.approvalStatus === ApprovalStatus.PENDING && (
                                    <div className="flex justify-end space-x-2">
                                        <button
                                            onClick={() => handleOpenModal(vacation)}
                                            className="text-green-600 hover:text-green-900"
                                        >
                                            <Check className="w-5 h-5" />
                                        </button>
                                        <button
                                            onClick={() => handleOpenModal(vacation)}
                                            className="text-red-600 hover:text-red-900"
                                        >
                                            <X className="w-5 h-5" />
                                        </button>
                                    </div>
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>

                {(!vacations?.data?.content || vacations.data.content.length === 0) && (
                    <div className="text-center py-8 text-gray-500">
                        조회된 휴가 신청이 없습니다.
                    </div>
                )}
            </div>

            {/* 승인/반려 모달 */}
            {isModalOpen && selectedVacation && (
                <div className="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md">
                        <h2 className="text-xl font-bold mb-4">휴가 승인/반려</h2>
                        <div className="space-y-4">
                            <div className="bg-gray-50 p-4 rounded">
                                <p className="text-sm text-gray-600">
                                    신청자: {selectedVacation.userName}
                                </p>
                                <p className="text-sm text-gray-600">
                                    기간: {dayjs(selectedVacation.startDate).format('YYYY-MM-DD')} ~
                                    {dayjs(selectedVacation.endDate).format('YYYY-MM-DD')}
                                </p>
                                <p className="text-sm text-gray-600">
                                    종류: {getVacationTypeText(selectedVacation.type)}
                                </p>
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">검토 의견</label>
                                <textarea
                                    value={approvalNote}
                                    onChange={(e) => setApprovalNote(e.target.value)}
                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:ring-indigo-500 focus:border-indigo-500"
                                    rows={4}
                                    placeholder="검토 의견을 입력하세요..."
                                />
                            </div>
                            <div className="flex justify-end space-x-2">
                                <button
                                    onClick={() => setIsModalOpen(false)}
                                    className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                                >
                                    취소
                                </button>
                                <button
                                    onClick={() => confirmApproval(ApprovalStatus.APPROVED)}
                                    className="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700"
                                >
                                    승인
                                </button>
                                <button
                                    onClick={() => confirmApproval(ApprovalStatus.REJECTED)}
                                    className="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700"
                                >
                                    반려
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default VacationApprovalPage;