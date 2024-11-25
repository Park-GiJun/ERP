import React from 'react';
import { BrowserRouter, Route, Routes, Navigate } from 'react-router-dom';
import { RecoilRoot } from 'recoil';
import LoginPage from '@/pages/auth/LoginPage';
import ProtectedRoute from '@/components/auth/ProtectedRoute';
import MainLayout from '@/components/layout/MainLayout';
import DashboardPage from '@/pages/dashboard/DashboardPage';
import RegisterPage from "@/pages/auth/RegisterPage.tsx";

const App: React.FC = () => {
    return (
        <RecoilRoot>
            <BrowserRouter>
                <Routes>
                    {/* 로그인 페이지 */}
                    <Route path="/login" element={<LoginPage />} />

                    <Route path="/register" element={<RegisterPage />} />

                    {/* 메인 레이아웃 */}
                    <Route
                        path="/"
                        element={
                            <ProtectedRoute>
                                <MainLayout />
                            </ProtectedRoute>
                        }
                    >
                        {/* 대시보드 */}
                        <Route index element={<Navigate to="/dashboard" replace />} />
                        <Route path="dashboard" element={<DashboardPage />} />
                    </Route>

                    {/* 404 페이지로 리디렉션 */}
                    <Route path="*" element={<Navigate to="/login" replace />} />
                </Routes>
            </BrowserRouter>
        </RecoilRoot>
    );
};

export default App;
