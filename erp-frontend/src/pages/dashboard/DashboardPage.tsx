import React, { useEffect, useState } from 'react';
import axios from '@/lib/axios';
import { ApiResponse } from '@/types/api';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import '@/styles/calendar.css';
import dayjs from 'dayjs';
import { Clock, CalendarDays, Briefcase, LogIn, LogOut, PlusCircle } from 'lucide-react';

const DashboardPage: React.FC = () => {
    const [summary, setSummary] = useState({
        attendance: { checkIn: '', checkOut: '' },
        annualLeave: 0,
        vacation: { pending: 0, approved: 0 },
    });
    const [vacationDates, setVacationDates] = useState([]);
    const [vacationList, setVacationList] = useState([]);
    const userId = JSON.parse(localStorage.getItem('userInfo') || '{}').id;
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const attendanceRes = await axios.get<ApiResponse<any>>(`/attendance/search/${userId}`, {
                    params: {
                        startDate: '2024-01-01T00:00:00',
                        endDate: '2024-12-31T23:59:59',
                        page: 0,
                        size: 5,
                    },
                });

                setSummary((prev) => ({
                    ...prev,
                    attendance: {
                        checkIn: dayjs(attendanceRes.data?.data?.content[0]?.checkIn).format('YYYY-MM-DD HH:mm') || 'N/A',
                        checkOut: dayjs(attendanceRes.data?.data?.content[0]?.checkOut).format('YYYY-MM-DD HH:mm') || 'N/A',
                    },
                }));

                const annualLeaveRes = await axios.get<ApiResponse<any>>(`/annual-leaves/${userId}`, {
                    params: { year: 2024 },
                });

                setSummary((prev) => ({
                    ...prev,
                    annualLeave: annualLeaveRes.data?.data?.totalDays - annualLeaveRes.data?.data?.usedDays || 0,
                }));

                const vacationRes = await axios.get<ApiResponse<any>>('/vacations', { params: { userId } });
                const vacations = vacationRes.data?.data?.content?.map((vacation: any) => ({
                    date: vacation.date,
                    status: vacation.approvalStatus,
                    reason: vacation.reason || '사유 없음',
                }));

                setVacationDates(vacations || []);
                setVacationList(vacations || []);

                setSummary((prev) => ({
                    ...prev,
                    vacation: {
                        pending: vacations?.filter((v: any) => v.status === 'PENDING').length || 0,
                        approved: vacations?.filter((v: any) => v.status === 'APPROVED').length || 0,
                    },
                }));
            } catch (error) {
                console.error('데이터를 가져오는 중 오류가 발생했습니다:', error);
            } finally {
                setIsLoading(false);
            }
        };

        if (userId) fetchData();
    }, [userId]);

    const tileClassName = ({ date }: { date: Date }) => {
        const formattedDate = dayjs(date).format('YYYY-MM-DD');
        const vacation = vacationDates.find((v: any) => v.date === formattedDate);

        // @ts-ignore
        if (vacation?.status === 'PENDING') return 'bg-amber-100 text-amber-600 rounded-full font-bold';
        // @ts-ignore
        if (vacation?.status === 'APPROVED') return 'bg-emerald-100 text-emerald-600 rounded-full font-bold';
        return '';
    };

    const handleCheckIn = async () => {
        try {
            const res = await axios.post('/attendance/check-in', { note: '출근 처리' });
            if (res.data?.success) {
                setSummary(prev => ({
                    ...prev,
                    attendance: {
                        ...prev.attendance,
                        checkIn: dayjs().format('YYYY-MM-DD HH:mm')
                    }
                }));
            }
            alert(res.data?.success ? '출근 처리 완료' : '출근 처리 실패');
        } catch (err) {
            alert('출근 처리 중 오류가 발생했습니다.');
        }
    };

    const handleCheckOut = async () => {
        try {
            const res = await axios.post('/attendance/check-out', { note: '퇴근 처리' });
            if (res.data?.success) {
                setSummary(prev => ({
                    ...prev,
                    attendance: {
                        ...prev.attendance,
                        checkOut: dayjs().format('YYYY-MM-DD HH:mm')
                    }
                }));
            }
            alert(res.data?.success ? '퇴근 처리 완료' : '퇴근 처리 실패');
        } catch (err) {
            alert('퇴근 처리 중 오류가 발생했습니다.');
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
        <div className="p-8 space-y-8 max-w-7xl mx-auto">
            {/* 출퇴근 및 휴가 신청 버튼 */}
            <div className="flex justify-end space-x-4">
                <button
                    onClick={handleCheckIn}
                    className="inline-flex items-center px-6 py-3 bg-blue-500 hover:bg-blue-600 text-white rounded-lg transition shadow-lg hover:shadow-xl"
                >
                    <LogIn className="w-5 h-5 mr-2" />
                    출근하기
                </button>
                <button
                    onClick={handleCheckOut}
                    className="inline-flex items-center px-6 py-3 bg-pink-500 hover:bg-pink-600 text-white rounded-lg transition shadow-lg hover:shadow-xl"
                >
                    <LogOut className="w-5 h-5 mr-2" />
                    퇴근하기
                </button>
                <button className="inline-flex items-center px-6 py-3 bg-emerald-500 hover:bg-emerald-600 text-white rounded-lg transition shadow-lg hover:shadow-xl">
                    <PlusCircle className="w-5 h-5 mr-2" />
                    휴가 신청
                </button>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                {/* 왼쪽 섹션: 상태 카드 */}
                <div className="space-y-6">
                    {/* 근태 카드 */}
                    <div className="bg-white rounded-2xl shadow-xl p-6 border border-gray-100">
                        <div className="flex items-center gap-4 mb-4">
                            <div className="p-3 bg-blue-100 rounded-lg">
                                <Clock className="w-6 h-6 text-blue-600" />
                            </div>
                            <h2 className="text-xl font-bold text-gray-800">근태 현황</h2>
                        </div>
                        <div className="grid grid-cols-2 gap-4">
                            <div className="p-4 bg-gray-50 rounded-lg">
                                <p className="text-sm text-gray-500 mb-1">출근 시각</p>
                                <p className="text-lg font-semibold text-gray-800">{summary.attendance.checkIn}</p>
                            </div>
                            <div className="p-4 bg-gray-50 rounded-lg">
                                <p className="text-sm text-gray-500 mb-1">퇴근 시각</p>
                                <p className="text-lg font-semibold text-gray-800">{summary.attendance.checkOut}</p>
                            </div>
                        </div>
                    </div>

                    {/* 연차 카드 */}
                    <div className="bg-white rounded-2xl shadow-xl p-6 border border-gray-100">
                        <div className="flex items-center gap-4 mb-4">
                            <div className="p-3 bg-emerald-100 rounded-lg">
                                <CalendarDays className="w-6 h-6 text-emerald-600" />
                            </div>
                            <h2 className="text-xl font-bold text-gray-800">연차 현황</h2>
                        </div>
                        <div className="p-4 bg-gray-50 rounded-lg">
                            <div className="flex justify-between items-center">
                                <p className="text-sm text-gray-500">잔여 연차</p>
                                <p className="text-2xl font-bold text-emerald-600">{summary.annualLeave}일</p>
                            </div>
                        </div>
                    </div>

                    {/* 휴가 신청 현황 카드 */}
                    <div className="bg-white rounded-2xl shadow-xl p-6 border border-gray-100">
                        <div className="flex items-center gap-4 mb-4">
                            <div className="p-3 bg-amber-100 rounded-lg">
                                <Briefcase className="w-6 h-6 text-amber-600" />
                            </div>
                            <h2 className="text-xl font-bold text-gray-800">휴가 신청 현황</h2>
                        </div>
                        <div className="grid grid-cols-2 gap-4">
                            <div className="p-4 bg-gray-50 rounded-lg">
                                <p className="text-sm text-gray-500 mb-1">대기 중</p>
                                <p className="text-lg font-semibold text-amber-600">{summary.vacation.pending}건</p>
                            </div>
                            <div className="p-4 bg-gray-50 rounded-lg">
                                <p className="text-sm text-gray-500 mb-1">승인됨</p>
                                <p className="text-lg font-semibold text-emerald-600">{summary.vacation.approved}건</p>
                            </div>
                        </div>
                    </div>
                </div>

                {/* 오른쪽 섹션: 캘린더 및 휴가 목록 */}
                <div className="space-y-6">
                    {/* 캘린더 */}
                    <div className="bg-white rounded-2xl shadow-xl p-6 border border-gray-100">
                        <Calendar
                            className="rounded-lg border-none"
                            tileClassName={tileClassName}
                        />
                    </div>

                    {/* 휴가 목록 */}
                    <div className="bg-white rounded-2xl shadow-xl p-6 border border-gray-100">
                        <h2 className="text-xl font-bold text-gray-800 mb-4">휴가 신청 내역</h2>
                        <div className="space-y-3 max-h-[300px] overflow-y-auto">
                            {vacationList.length > 0 ? (
                                vacationList.map((vacation: any, index: number) => (
                                    <div
                                        key={index}
                                        className="p-4 bg-gray-50 rounded-lg flex items-center justify-between"
                                    >
                                        <div>
                                            <p className="text-sm text-gray-600">
                                                {dayjs(vacation.date).format('YYYY-MM-DD')}
                                            </p>
                                            <p className="text-sm text-gray-500">{vacation.reason}</p>
                                        </div>
                                        <span
                                            className={`px-3 py-1 rounded-full text-sm font-medium
                                                ${vacation.status === 'PENDING'
                                                ? 'bg-amber-100 text-amber-700'
                                                : 'bg-emerald-100 text-emerald-700'
                                            }`}
                                        >
                                            {vacation.status === 'PENDING' ? '대기' : '승인'}
                                        </span>
                                    </div>
                                ))
                            ) : (
                                <p className="text-center text-gray-500 py-4">
                                    등록된 휴가가 없습니다
                                </p>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default DashboardPage;