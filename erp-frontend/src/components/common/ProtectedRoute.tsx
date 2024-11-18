// components/common/ProtectedRoute.tsx
import { Navigate } from 'react-router-dom';
import React from "react";

interface Props {
    children: React.ReactNode;
}

const ProtectedRoute = ({ children }: Props) => {
    const accessToken = localStorage.getItem('accessToken');

    if (!accessToken) {
        return <Navigate to="/login" replace />;
    }

    return <>{children}</>;
};

export default ProtectedRoute;