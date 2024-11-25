import React, { useEffect, useState } from 'react';
import axios from '@/lib/axios';
import { ApiResponse } from '@/types/api';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import '@/styles/calendar.css';
import dayjs from 'dayjs';

const DashboardPage: React.FC = () => {
    const [summary, setSummary] = useState({
        attendance: { checkIn: '', checkOut: '' },
        annualLeave: 0,
        vacation: { pending: 0, approved: 0 },
    });
    const [vacationDates, setVacationDates] = useState([]);
    const [vacationList, setVacationList] = useState([]);
    const userId = JSON.parse(localStorage.getItem('userInfo') || '{}').id;

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
            }
        };

        if (userId) fetchData();
    }, [userId]);

    const tileClassName = ({ date }: { date: Date }) => {
        const formattedDate = dayjs(date).format('YYYY-MM-DD');
        const vacation = vacationDates.find((v: any) => v.date === formattedDate);

        // @ts-ignore
        if (vacation?.status === 'PENDING') return 'bg-red-500 text-white rounded-full';
        // @ts-ignore
        if (vacation?.status === 'APPROVED') return 'bg-green-500 text-white rounded-full';
        return '';
    };

    return (
        <div className="p-6 h-screen flex flex-col gap-6">
            {/* 버튼 섹션 */}
            <div className="flex justify-end gap-4">
                <button
                    className="bg-indigo-500 hover:bg-indigo-600 text-white font-semibold py-3 px-6 rounded-lg shadow transition"
                    onClick={async () => {
                        try {
                            const res = await axios.post('/attendance/check-in', { note: '출근 처리' });
                            alert(res.data?.success ? '출근 처리 완료' : '출근 처리 실패');
                        } catch (err) {
                            alert('출근 처리 중 오류가 발생했습니다.');
                        }
                    }}
                >
                    출근
                </button>
                <button
                    className="bg-red-500 hover:bg-red-600 text-white font-semibold py-3 px-6 rounded-lg shadow transition"
                    onClick={async () => {
                        try {
                            const res = await axios.post('/attendance/check-out', { note: '퇴근 처리' });
                            alert(res.data?.success ? '퇴근 처리 완료' : '퇴근 처리 실패');
                        } catch (err) {
                            alert('퇴근 처리 중 오류가 발생했습니다.');
                        }
                    }}
                >
                    퇴근
                </button>
                <button className="bg-green-500 hover:bg-green-600 text-white font-semibold py-3 px-6 rounded-lg shadow transition">
                    휴가 신청
                </button>
            </div>

            {/* 카드 및 달력 섹션 */}
            <div className="flex justify-between gap-6 items-start">
                {/* 카드 섹션 */}
                <div className="flex flex-col gap-4 flex-1">
                    <div className="bg-gradient-to-r from-blue-400 to-indigo-500 text-white rounded-lg p-4">
                        <h2 className="text-sm font-semibold">근태</h2>
                        <p className="mt-2 text-sm">출근: <span className="font-bold">{summary.attendance.checkIn}</span></p>
                        <p className="text-sm">퇴근: <span className="font-bold">{summary.attendance.checkOut}</span></p>
                    </div>
                    <div className="bg-gradient-to-r from-green-400 to-teal-500 text-white rounded-lg p-4">
                        <h2 className="text-sm font-semibold">연차</h2>
                        <p className="mt-2 text-sm">사용 가능: <span className="font-bold">{summary.annualLeave}일</span></p>
                    </div>
                    <div className="bg-gradient-to-r from-yellow-400 to-orange-500 text-white rounded-lg p-4">
                        <h2 className="text-sm font-semibold">휴가</h2>
                        <p className="mt-2 text-sm">대기: <span className="font-bold">{summary.vacation.pending}건</span></p>
                        <p className="text-sm">승인: <span className="font-bold">{summary.vacation.approved}건</span></p>
                    </div>
                </div>

                {/* 달력 및 휴가 목록 섹션 */}
                <div className="flex-1 flex flex-col gap-6">
                    <div className="bg-white shadow-lg rounded-lg p-6">
                        <Calendar
                            className="rounded-lg"
                            tileClassName={tileClassName}
                        />
                    </div>
                    <div className="bg-white shadow-lg rounded-lg p-4 h-48 overflow-y-auto">
                        <h2 className="text-sm font-semibold mb-4">휴가 목록</h2>
                        {vacationList.length > 0 ? (
                            <ul className="space-y-2">
                                {vacationList.map((vacation: any, index: number) => (
                                    <li key={index} className="text-sm text-gray-700">
                                        <span className={`font-bold ${vacation.status === 'PENDING' ? 'text-red-500' : 'text-green-500'}`}>
                                            {vacation.status === 'PENDING' ? '대기' : '승인'}
                                        </span>
                                        {' - '}
                                        {dayjs(vacation.date).format('YYYY-MM-DD')} ({vacation.reason})
                                    </li>
                                ))}
                            </ul>
                        ) : (
                            <p className="text-sm text-gray-500">등록된 휴가가 없습니다.</p>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default DashboardPage;
