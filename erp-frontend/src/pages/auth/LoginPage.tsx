import React, { useState } from 'react';
import { useRecoilState } from 'recoil';
import { authTokenState, refreshTokenState, userInfoState } from '@/store/authState';
import axios from '@/lib/axios';
import {NavLink, useNavigate} from 'react-router-dom';
import { LoginRequest, LoginResponse, ApiResponse } from '@/types/api';

const LoginPage: React.FC = () => {
    const [, setAuthToken] = useRecoilState(authTokenState);
    const [, setRefreshToken] = useRecoilState(refreshTokenState);
    const [, setUserInfo] = useRecoilState(userInfoState);
    const navigate = useNavigate();

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const handleLogin = async () => {
        try {
            const response = await axios.post<ApiResponse<LoginResponse>>('/public/auth/login', {
                email,
                password,
            } as LoginRequest);

            if (response.data.success) {
                const { accessToken, refreshToken, userInfo } = response.data.data;
                setAuthToken(accessToken);
                setRefreshToken(refreshToken);
                setUserInfo(userInfo);
                alert(`환영합니다, ${userInfo.name}!`);
                navigate('/dashboard'); // 로그인 후 대시보드로 이동
            } else {
                setErrorMessage(response.data.error?.message || '알 수 없는 오류가 발생했습니다.');
            }
        } catch (error) {
            setErrorMessage('로그인 실패: 이메일과 비밀번호를 확인해주세요.');
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-gradient-to-br from-gray-100 to-gray-300">
            <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
                <h2 className="text-3xl font-semibold text-center text-gray-800 mb-6">로그인</h2>
                {errorMessage && (
                    <div className="text-red-600 text-sm mb-4 text-center bg-red-100 p-2 rounded">
                        {errorMessage}
                    </div>
                )}
                <div className="space-y-4">
                    <input
                        type="email"
                        placeholder="이메일"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <input
                        type="password"
                        placeholder="비밀번호"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>
                <button
                    onClick={handleLogin}
                    className="w-full mt-6 bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded-lg transition duration-200"
                >
                    로그인
                </button>
                <p className="mt-4 text-center text-sm text-gray-600">
                    계정이 없으신가요?{' '}
                    <NavLink to="/register" className="text-blue-500 hover:underline">
                        회원가입
                    </NavLink>
                </p>
                <div className="mt-6">
                    <a
                        href="http://15.165.163.233:9832/swagger-ui/index.html"
                        target="_blank"
                        rel="noopener noreferrer"
                        className="block w-full text-center bg-gray-100 hover:bg-gray-200 text-gray-700 font-medium py-2 px-4 rounded-lg transition duration-200"
                    >
                        Swagger UI로 이동
                    </a>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;
