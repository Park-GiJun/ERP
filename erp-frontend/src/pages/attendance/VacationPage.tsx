import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import axios from '@/lib/axios';
import { Calendar, Info, Clock, AlertCircle } from 'lucide-react';
import dayjs from 'dayjs';
import {
    VacationType,
    VacationRequest,
    VacationBalance,
    ApprovalStatus, VacationPage
} from '@/types/vacation';
import { ApiResponse } from '@/types/api';

const VacationRequestPage = () => {
    const queryClient = useQueryClient();
    const userId = JSON.parse(localStorage.getItem('userInfo') || '{}').id;

    const [request, setRequest] = useState<VacationRequest>({
        startDate: dayjs().format('YYYY-MM-DD'),
        endDate: dayjs().format('YYYY-MM-DD'),
        type: VacationType.ANNUAL,
        reason: ''
    });

    // 휴가 잔여일수 조회
    const { data: balance } = useQuery<ApiResponse<VacationBalance>>({
        queryKey: ['vacation-balance', userId],
        queryFn: async () => {
            const response = await axios.get(`/annual-leaves/${userId}`, {
                params: { year: new Date().getFullYear() }
            });
            return response.data;
        }
    });

    // 내 휴가 신청 내역 조회
    const { data: myVacations } = useQuery<ApiResponse<VacationPage>>({
        queryKey: ['my-vacations', userId],
        queryFn: async () => {
            const response = await axios.get('/vacations', {
                params: {
                    userId,
                    page: 0,
                    size: 10
                }
            });
            return response.data;
        }
    });

    // 휴가 신청 mutation
    const mutation = useMutation({
        mutationFn: async (data: VacationRequest) => {
            return axios.post('/vacations', data, {
                params: { userId }
            });
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['my-vacations'] });
            queryClient.invalidateQueries({ queryKey: ['vacation-balance'] });
            alert('휴가 신청이 완료되었습니다.');
            resetForm();
        },
        onError: () => {
            alert('휴가 신청 중 오류가 발생했습니다.');
        }
    });

    const resetForm = () => {
        setRequest({
            startDate: `${dayjs().format('YYYY-MM-DD')}T00:00:00`,
            endDate: `${dayjs().format('YYYY-MM-DD')}T23:59:59`,
            type: VacationType.ANNUAL,
            reason: '',
        });
    };


    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (!request.reason.trim()) {
            alert('휴가 사유를 입력해주세요.');
            return;
        }
        mutation.mutate(request);
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

    return (
        <div className="p-8 space-y-8 max-w-7xl mx-auto">
            <h1 className="text-2xl font-bold text-gray-900">휴가 신청</h1>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                {/* 휴가 신청 폼 */}
                <div className="space-y-6">
                    <div className="bg-white rounded-xl shadow-lg p-6 border border-gray-100">
                        <div className="flex items-center gap-4 mb-6">
                            <div className="p-3 bg-blue-100 rounded-lg">
                                <Calendar className="w-6 h-6 text-blue-600" />
                            </div>
                            <h2 className="text-xl font-bold text-gray-800">휴가 신청서</h2>
                        </div>

                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">시작일</label>
                                    <input
                                        type="date"
                                        value={request.startDate}
                                        onChange={(e) => setRequest(prev => ({ ...prev, startDate: e.target.value }))}
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">종료일</label>
                                    <input
                                        type="date"
                                        value={request.endDate}
                                        onChange={(e) => setRequest(prev => ({ ...prev, endDate: e.target.value  }))}
                                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                        required
                                    />
                                </div>
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">휴가 종류</label>
                                <select
                                    value={request.type}
                                    onChange={(e) => setRequest(prev => ({ ...prev, type: e.target.value as VacationType }))}
                                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                    required
                                >
                                    {Object.values(VacationType).map((type) => (
                                        <option key={type} value={type}>
                                            {getVacationTypeText(type)}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">신청 사유</label>
                                <textarea
                                    value={request.reason}
                                    onChange={(e) => setRequest(prev => ({ ...prev, reason: e.target.value }))}
                                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                                    rows={4}
                                    required
                                />
                            </div>

                            <div className="flex justify-end">
                                <button
                                    type="submit"
                                    className="px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition shadow-md"
                                    disabled={mutation.isPending}
                                >
                                    {mutation.isPending ? '신청 중...' : '휴가 신청'}
                                </button>
                            </div>
                        </form>
                    </div>

                    {/* 잔여 휴가 정보 */}
                    <div className="bg-white rounded-xl shadow-lg p-6 border border-gray-100">
                        <div className="flex items-center gap-4 mb-6">
                            <div className="p-3 bg-emerald-100 rounded-lg">
                                <Info className="w-6 h-6 text-emerald-600" />
                            </div>
                            <h2 className="text-xl font-bold text-gray-800">잔여 휴가</h2>
                        </div>

                        <div className="grid grid-cols-3 gap-4">
                            <div className="p-4 bg-gray-50 rounded-lg">
                                <p className="text-sm text-gray-500">총 연차</p>
                                <p className="text-xl font-bold text-gray-900">{balance?.data?.totalDays || 0}일</p>
                            </div>
                            <div className="p-4 bg-gray-50 rounded-lg">
                                <p className="text-sm text-gray-500">사용</p>
                                <p className="text-xl font-bold text-gray-900">{balance?.data?.usedDays || 0}일</p>
                            </div>
                            <div className="p-4 bg-gray-50 rounded-lg">
                                <p className="text-sm text-gray-500">잔여</p>
                                <p className="text-xl font-bold text-emerald-600">{balance?.data?.remainingDays || 0}일</p>
                            </div>
                        </div>
                    </div>
                </div>

                {/* 신청 내역 */}
                <div className="bg-white rounded-xl shadow-lg p-6 border border-gray-100">
                    <div className="flex items-center gap-4 mb-6">
                        <div className="p-3 bg-purple-100 rounded-lg">
                            <Clock className="w-6 h-6 text-purple-600" />
                        </div>
                        <h2 className="text-xl font-bold text-gray-800">휴가 신청 내역</h2>
                    </div>

                    <div className="space-y-4 max-h-[600px] overflow-y-auto">
                        {myVacations?.data?.content?.length === 0 ? (
                            <div className="text-center py-8 text-gray-500">
                                신청한 휴가가 없습니다.
                            </div>
                        ) : (
                            myVacations?.data?.content?.map((vacation) => (
                                <div
                                    key={vacation.id}
                                    className="p-4 bg-gray-50 rounded-lg space-y-2"
                                >
                                    <div className="flex justify-between items-start">
                                        <div>
                                            <p className="text-sm font-medium text-gray-900">
                                                {dayjs(vacation.startDate).format('YYYY-MM-DD')} ~
                                                {dayjs(vacation.endDate).format('YYYY-MM-DD')}
                                            </p>
                                            <p className="text-sm text-gray-600">
                                                {getVacationTypeText(vacation.type)}
                                            </p>
                                        </div>
                                        <span className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(vacation.approvalStatus)}`}>
                                            {getStatusText(vacation.approvalStatus)}
                                        </span>
                                    </div>
                                    <p className="text-sm text-gray-500">{vacation.reason}</p>
                                    {vacation.approverNote && (
                                        <div className="flex items-start gap-2 text-sm text-gray-500 bg-gray-100 p-2 rounded">
                                            <AlertCircle className="w-4 h-4 mt-0.5" />
                                            <p>{vacation.approverNote}</p>
                                        </div>
                                    )}
                                </div>
                            ))
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default VacationRequestPage;