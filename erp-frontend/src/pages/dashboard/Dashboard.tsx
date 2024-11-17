import React from 'react';
import { useUserProfile } from '../../hooks/queries/useUserQuery';
import { useVacationList } from '../../hooks/queries/useVacationQuery';

const Dashboard = () => {
    const { data: userInfo, isLoading: userLoading } = useUserProfile();
    const { data: vacationData, isLoading: vacationLoading } = useVacationList();

    if (userLoading || vacationLoading) {
        return <div>Loading...</div>;
    }

    return (
        <div className="space-y-6">
            {/* 환영 메시지 */}
            <div className="bg-white p-6 rounded-lg shadow">
                <h1 className="text-2xl font-semibold text-gray-900">
                    안녕하세요, {userInfo?.data.name}님!
                </h1>
                <p className="mt-2 text-gray-600">
                    오늘도 좋은 하루 되세요.
                </p>
            </div>

            {/* 주요 정보 카드 */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                {/* 휴가 정보 */}
                <div className="bg-white p-6 rounded-lg shadow">
                    <h2 className="text-lg font-medium text-gray-900">휴가 현황</h2>
                    <div className="mt-4">
                        <div className="text-3xl font-bold text-indigo-600">
                            {/* 휴가 잔여일수 등 표시 */}
                            15일
                        </div>
                        <p className="mt-1 text-sm text-gray-500">남은 연차</p>
                    </div>
                </div>

                {/* 근태 현황 */}
                <div className="bg-white p-6 rounded-lg shadow">
                    <h2 className="text-lg font-medium text-gray-900">이번 주 근무시간</h2>
                    <div className="mt-4">
                        <div className="text-3xl font-bold text-indigo-600">
                            40시간
                        </div>
                        <p className="mt-1 text-sm text-gray-500">누적 근무시간</p>
                    </div>
                </div>

                {/* 공지사항 */}
                <div className="bg-white p-6 rounded-lg shadow">
                    <h2 className="text-lg font-medium text-gray-900">공지사항</h2>
                    <div className="mt-4">
                        <div className="text-sm text-gray-600">
                            최근 공지사항이 없습니다.
                        </div>
                    </div>
                </div>
            </div>

            {/* 최근 휴가 신청 현황 */}
            <div className="bg-white p-6 rounded-lg shadow">
                <h2 className="text-lg font-medium text-gray-900 mb-4">
                    최근 휴가 신청 현황
                </h2>
                <div className="overflow-x-auto">
                    <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                기간
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                종류
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                상태
                            </th>
                        </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-200">
                        {vacationData?.data.content.map((vacation) => (
                            <tr key={vacation.id}>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {new Date(vacation.startDate).toLocaleDateString()} ~
                                    {new Date(vacation.endDate).toLocaleDateString()}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {vacation.type}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full 
                      ${
                        vacation.approvalStatus === 'APPROVED'
                            ? 'bg-green-100 text-green-800'
                            : vacation.approvalStatus === 'PENDING'
                                ? 'bg-yellow-100 text-yellow-800'
                                : 'bg-red-100 text-red-800'
                    }
                    `}>
                      {vacation.approvalStatus}
                    </span>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;