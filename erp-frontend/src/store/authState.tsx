import { atom } from 'recoil';

// Access Token 상태
export const authTokenState = atom<string | null>({
    key: 'authTokenState',
    default: localStorage.getItem('accessToken'),
    effects: [
        ({ onSet }) => {
            onSet((newToken) => {
                if (newToken) {
                    localStorage.setItem('accessToken', newToken);
                } else {
                    localStorage.removeItem('accessToken');
                }
            });
        },
    ],
});

// Refresh Token 상태
export const refreshTokenState = atom<string | null>({
    key: 'refreshTokenState',
    default: localStorage.getItem('refreshToken'),
    effects: [
        ({ onSet }) => {
            onSet((newToken) => {
                if (newToken) {
                    localStorage.setItem('refreshToken', newToken);
                } else {
                    localStorage.removeItem('refreshToken');
                }
            });
        },
    ],
});

// User Info 상태
export const userInfoState = atom<Record<string, any> | null>({
    key: 'userInfoState',
    default: JSON.parse(localStorage.getItem('userInfo') || 'null'),
    effects: [
        ({ onSet }) => {
            onSet((newUserInfo) => {
                if (newUserInfo) {
                    localStorage.setItem('userInfo', JSON.stringify(newUserInfo));
                } else {
                    localStorage.removeItem('userInfo');
                }
            });
        },
    ],
});
