import React from 'react';
import { Menu } from '@headlessui/react';
import { UserCircleIcon, BellIcon } from '@heroicons/react/24/outline';

const Header: React.FC = () => {
    return (
        <header className="bg-white shadow">
            <div className="flex justify-between items-center px-4 py-3">
                <div className="flex items-center">
                    <h1 className="text-xl font-semibold text-gray-800">
                        {/* 현재 페이지 제목 */}
                    </h1>
                </div>

                <div className="flex items-center space-x-4">
                    {/* 알림 */}
                    <button className="p-1 rounded-full hover:bg-gray-100">
                        <BellIcon className="h-6 w-6 text-gray-600" />
                    </button>

                    {/* 사용자 메뉴 */}
                    <Menu as="div" className="relative">
                        <Menu.Button className="flex items-center">
                            <UserCircleIcon className="h-8 w-8 text-gray-600" />
                        </Menu.Button>

                        <Menu.Items className="absolute right-0 mt-2 w-48 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5">
                            <Menu.Item>
                                {({ active }) => (
                                    <a
                                        href="/profile"
                                        className={`
                      ${active ? 'bg-gray-100' : ''}
                      block px-4 py-2 text-sm text-gray-700
                    `}
                                    >
                                        프로필
                                    </a>
                                )}
                            </Menu.Item>
                            <Menu.Item>
                                {({ active }) => (
                                    <button
                                        onClick={() => {/* 로그아웃 처리 */}}
                                        className={`
                      ${active ? 'bg-gray-100' : ''}
                      block w-full text-left px-4 py-2 text-sm text-gray-700
                    `}
                                    >
                                        로그아웃
                                    </button>
                                )}
                            </Menu.Item>
                        </Menu.Items>
                    </Menu>
                </div>
            </div>
        </header>
    );
};

export default Header;