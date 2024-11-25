import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';

const Sidebar: React.FC = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        // 로컬 스토리지에서 인증 정보 제거
        localStorage.removeItem('accessToken');
        localStorage.removeItem('userInfo');
        localStorage.removeItem('refreshToken');

        // 로그아웃 후 로그인 페이지로 이동
        navigate('/login');
    };

    const menuItems = [
        { name: '대시보드', path: '/dashboard' },
        { name: '근태 관리', path: '/attendance' },
        { name: '휴가 관리', path: '/vacations' },
        { name: '직급 관리', path: '/positions' },
        { name: '부서 관리', path: '/departments' },
    ];

    return (
        <aside className="w-64 bg-white shadow-md flex flex-col">
            <div className="p-4 border-b">
                <h1 className="text-xl font-bold text-gray-700">ERP 시스템</h1>
            </div>
            <nav className="flex-1 p-4">
                <ul className="space-y-2">
                    {menuItems.map((item) => (
                        <li key={item.path}>
                            <NavLink
                                to={item.path}
                                className={({ isActive }) =>
                                    `block p-2 rounded-lg ${
                                        isActive ? 'bg-indigo-500 text-white' : 'text-gray-700 hover:bg-gray-100'
                                    }`
                                }
                            >
                                {item.name}
                            </NavLink>
                        </li>
                    ))}
                </ul>
            </nav>
            <div className="p-4 border-t">
                <button
                    onClick={handleLogout}
                    className="w-full py-2 px-4 text-sm text-red-600 bg-red-100 rounded-lg hover:bg-red-200"
                >
                    로그아웃
                </button>
            </div>
        </aside>
    );
};

export default Sidebar;
