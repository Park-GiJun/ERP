import React, { useState } from 'react';
import axios from '@/lib/axios';
import { ApiResponse } from '@/types/api';
import { useNavigate } from 'react-router-dom';

const RegisterPage: React.FC = () => {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
        confirmPassword: '',
        name: '',
        employeeNumber: '',
        phoneNumber: '',
        departmentId: '',
        positionId: '',
    });
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const navigate = useNavigate();

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };



    const handleRegister = async () => {
        const {
            email,
            password,
            confirmPassword,
            name,
            employeeNumber,
            phoneNumber,
            departmentId,
            positionId,
        } = formData;

        // 간단한 유효성 검사
        if (!email || !password || !confirmPassword || !name || !employeeNumber || !phoneNumber) {
            setErrorMessage('모든 필드를 입력해주세요.');
            return;
        }
        if (password !== confirmPassword) {
            setErrorMessage('비밀번호가 일치하지 않습니다.');
            return;
        }

        try {
            const response = await axios.post<ApiResponse<any>>('/public/auth/register', {
                email,
                password,
                confirmPassword,
                name,
                employeeNumber,
                phoneNumber,
                departmentId: parseInt(departmentId, 10),
                positionId: parseInt(positionId, 10),
            });

            if (response.data.success) {
                setSuccessMessage('회원가입에 성공했습니다! 로그인 페이지로 이동합니다.');
                setTimeout(() => {
                    navigate('/login');
                }, 2000); // 2초 후 로그인 페이지로 이동
            } else {
                setErrorMessage(response.data.error?.message || '회원가입에 실패했습니다.');
            }
        } catch (error) {
            setErrorMessage('회원가입 중 오류가 발생했습니다.');
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-gradient-to-br from-gray-100 to-gray-300">
            <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
                <h2 className="text-3xl font-semibold text-center text-gray-800 mb-6">회원가입</h2>
                {errorMessage && (
                    <div className="text-red-600 text-sm mb-4 text-center bg-red-100 p-2 rounded">
                        {errorMessage}
                    </div>
                )}
                {successMessage && (
                    <div className="text-green-600 text-sm mb-4 text-center bg-green-100 p-2 rounded">
                        {successMessage}
                    </div>
                )}
                <div className="space-y-4">
                    <input
                        type="email"
                        name="email"
                        placeholder="이메일"
                        value={formData.email}
                        onChange={handleInputChange}
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <input
                        type="password"
                        name="password"
                        placeholder="비밀번호"
                        value={formData.password}
                        onChange={handleInputChange}
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <input
                        type="password"
                        name="confirmPassword"
                        placeholder="비밀번호 확인"
                        value={formData.confirmPassword}
                        onChange={handleInputChange}
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <input
                        type="text"
                        name="name"
                        placeholder="이름"
                        value={formData.name}
                        onChange={handleInputChange}
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <input
                        type="text"
                        name="employeeNumber"
                        placeholder="사번"
                        value={formData.employeeNumber}
                        onChange={handleInputChange}
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <input
                        type="text"
                        name="phoneNumber"
                        placeholder="전화번호"
                        value={formData.phoneNumber}
                        onChange={handleInputChange}
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <select
                        name="departmentId"
                        value={formData.departmentId}
                        onChange={handleInputChange}
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                        <option value="">부서 선택</option>
                        <option value="1">개발팀</option>
                        <option value="2">인사팀</option>
                    </select>
                    <select
                        name="positionId"
                        value={formData.positionId}
                        onChange={handleInputChange}
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                        <option value="">직급 선택</option>
                        <option value="5">사원</option>
                        <option value="4">대리</option>
                        <option value="3">과장</option>
                        <option value="2">부장</option>
                        <option value="1">CEO</option>
                    </select>
                </div>
                <button
                    onClick={handleRegister}
                    className="w-full mt-6 bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded-lg transition duration-200"
                >
                    회원가입
                </button>
                <p className="mt-4 text-center text-sm text-gray-600">
                    이미 계정이 있으신가요?{' '}
                    <a href="/login" className="text-blue-500 hover:underline">
                        로그인
                    </a>
                </p>
            </div>
        </div>
    );
};

export default RegisterPage;
