import React from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';

const MainLayout: React.FC = () => {
    return (
        <div className="flex h-screen">
            {/* 사이드바 */}
            <Sidebar />

            {/* 메인 콘텐츠 */}
            <div className="flex-1 bg-gray-100 overflow-auto">
                <Outlet />
            </div>
        </div>
    );
};

export default MainLayout;
