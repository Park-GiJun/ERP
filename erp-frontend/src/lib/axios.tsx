import axios from 'axios';
import type { AxiosResponse, InternalAxiosRequestConfig } from 'axios';
import type { ApiResponse } from '@/types/api';

const instance = axios.create({
    // baseURL: 'http://localhost:9832/api',
    baseURL: 'https://olm.life/api',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    },
});

// 요청 인터셉터
instance.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 응답 인터셉터
instance.interceptors.response.use(
    (response: AxiosResponse<ApiResponse<any>>) => {
        if (!response.data.success) {
            return Promise.reject({
                code: response.data.error?.code || 'UNKNOWN_ERROR',
                message: response.data.error?.message || 'Unknown error occurred',
            });
        }
        return response; // AxiosResponse 타입 유지
    },
    async (error) => {
        const { response } = error;

        if (response?.status === 401) {
            // 인증 에러 발생 시 처리
            localStorage.removeItem('accessToken');
            alert('인증이 만료되었습니다. 다시 로그인해주세요.');
            window.location.href = '/login';
        }

        return Promise.reject(response?.data || error);
    }
);

export default instance;
