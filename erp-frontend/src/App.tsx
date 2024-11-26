import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { RecoilRoot } from 'recoil';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import LoginPage from '@/pages/auth/LoginPage';
import RegisterPage from '@/pages/auth/RegisterPage';
import ProtectedRoute from '@/components/auth/ProtectedRoute';
import MainLayout from '@/components/layout/MainLayout';
import DashboardPage from '@/pages/dashboard/DashboardPage';
import DepartmentPage from '@/pages/organization/DepartmentPage';
import PositionPage from '@/pages/organization/PositionPage';
import AttendancePage from '@/pages/attendance/AttendancePage';
import VacationPage from '@/pages/attendance/VacationPage';
import VacationApprovalPage from '@/pages/attendance/VacationApprovalPage';

// Create a client
const queryClient = new QueryClient();

const App: React.FC = () => {
    return (
        <QueryClientProvider client={queryClient}>
            <RecoilRoot>
                <BrowserRouter>
                    <Routes>
                        {/* Public routes */}
                        <Route path="/login" element={<LoginPage />} />
                        <Route path="/register" element={<RegisterPage />} />

                        {/* Protected routes */}
                        <Route
                            path="/"
                            element={
                                <ProtectedRoute>
                                    <MainLayout />
                                </ProtectedRoute>
                            }
                        >
                            <Route index element={<Navigate to="/dashboard" replace />} />
                            <Route path="dashboard" element={<DashboardPage />} />

                            {/* Organization routes */}
                            <Route path="organization">
                                <Route path="departments" element={<DepartmentPage />} />
                                <Route path="positions" element={<PositionPage />} />
                            </Route>

                            {/* Attendance routes */}
                            <Route path="attendance">
                                <Route path="status" element={<AttendancePage />} />
                                <Route path="vacation" element={<VacationPage />} />
                                <Route path="approval" element={<VacationApprovalPage />} />
                            </Route>
                        </Route>

                        {/* Fallback route */}
                        <Route path="*" element={<Navigate to="/dashboard" replace />} />
                    </Routes>
                </BrowserRouter>
            </RecoilRoot>
        </QueryClientProvider>
    );
};

export default App;