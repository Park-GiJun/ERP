// router.tsx
import React, { Suspense } from 'react';
import { createBrowserRouter, Navigate } from 'react-router-dom';
import Layout from '../components/layout/Layout';
import ProtectedRoute from '../components/common/ProtectedRoute';

// Pages
const Dashboard = React.lazy(() => import('../pages/dashboard/Dashboard'));
const Login = React.lazy(() => import('../pages/auth/Login'));

const router = createBrowserRouter([
    {
        path: '/',
        element: (
            <ProtectedRoute>
                <Suspense fallback={<div>Loading...</div>}>
                    <Layout />
                </Suspense>
            </ProtectedRoute>
        ),
        children: [
            {
                index: true,
                element: <Navigate to="/dashboard" replace />
            },
            {
                path: 'dashboard',
                element: (
                    <Suspense fallback={<div>Loading...</div>}>
                        <Dashboard />
                    </Suspense>
                ),
            }
        ],
    },
    {
        path: '/login',
        element: (
            <Suspense fallback={<div>Loading...</div>}>
                <Login />
            </Suspense>
        ),
    },
    {
        path: '*',
        element: <Navigate to="/login" replace />,
    },
]);

export default router;