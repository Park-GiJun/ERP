import React from 'react';
import { createBrowserRouter } from 'react-router-dom';
import Layout from '../components/layout/Layout';

// Pages
const Dashboard = React.lazy(() => import('../pages/dashboard/Dashboard'));
// const UserList = React.lazy(() => import('../pages/hr/UserList'));
// const DepartmentList = React.lazy(() => import('../pages/hr/DepartmentList'));
// const PositionList = React.lazy(() => import('../pages/hr/PositionList'));
// const AttendanceStatus = React.lazy(() => import('../pages/attendance/AttendanceStatus'));
// const VacationRequest = React.lazy(() => import('../pages/attendance/VacationRequest'));
// const VacationApproval = React.lazy(() => import('../pages/attendance/VacationApproval'));
// const Settings = React.lazy(() => import('../pages/settings/Settings'));
const Login = React.lazy(() => import('../pages/auth/Login'));

const router = createBrowserRouter([
    {
        path: '/',
        element: <Layout />,
        children: [
            { path: 'dashboard', element: <Dashboard /> },
            // { path: 'hr/users', element: <UserList /> },
            // { path: 'hr/departments', element: <DepartmentList /> },
            // { path: 'hr/positions', element: <PositionList /> },
            // { path: 'attendance/status', element: <AttendanceStatus /> },
            // { path: 'attendance/vacation', element: <VacationRequest /> },
            // { path: 'attendance/approval', element: <VacationApproval /> },
            // { path: 'settings', element: <Settings /> },
        ],
    },
    {
        path: '/login',
        element: <Login />
    }
]);

export default router;