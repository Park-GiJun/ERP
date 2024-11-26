import { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import {
    Home,
    Users,
    Calendar,
    Building2,
    Settings,
} from 'lucide-react';

const navigation = [
    { name: '대시보드', path: '/dashboard', icon: Home },
    {
        name: '조직 관리',
        path: '/organization',
        icon: Building2,
        children: [
            { name: '부서 관리', path: '/organization/departments' },
            { name: '직급 관리', path: '/organization/positions' }
        ]
    },
    {
        name: '인사 관리',
        path: '/hr',
        icon: Users,
        children: [
            { name: '사용자 관리', path: '/hr/users' }
        ]
    },
    {
        name: '근태/휴가',
        path: '/attendance',
        icon: Calendar,
        children: [
            { name: '근태 현황', path: '/attendance/status' },
            { name: '휴가 신청', path: '/attendance/vacation' },
            { name: '휴가 승인', path: '/attendance/approval' }
        ]
    },
    { name: '설정', path: '/settings', icon: Settings }
];

const Sidebar = () => {
    const location = useLocation();
    const [expandedItems, setExpandedItems] = useState<string[]>([]);

    const toggleExpand = (path: string) => {
        setExpandedItems(prev =>
            prev.includes(path)
                ? prev.filter(item => item !== path)
                : [...prev, path]
        );
    };

    const isActive = (path: string) => location.pathname === path;
    const isParentActive = (item: any) => {
        if (item.children) {
            return item.children.some((child: any) => location.pathname.startsWith(child.path));
        }
        return location.pathname === item.path;
    };

    // Automatically expand parent when child is active
    useState(() => {
        navigation.forEach(item => {
            if (item.children && item.children.some(child => location.pathname.startsWith(child.path))) {
                setExpandedItems(prev => [...prev, item.path]);
            }
        });
    });

    return (
        <div className="flex flex-col w-64 bg-gray-800 text-white min-h-screen">
            <div className="flex items-center justify-center h-16 border-b border-gray-700">
                <span className="text-xl font-semibold">ERP System</span>
            </div>

            <nav className="flex-1 overflow-y-auto py-4">
                {navigation.map((item) => (
                    <div key={item.path}>
                        <div
                            onClick={() => item.children && toggleExpand(item.path)}
                            className={`
                                flex items-center px-4 py-2 cursor-pointer
                                ${isParentActive(item) ? 'bg-gray-700' : 'hover:bg-gray-700'}
                                transition-colors duration-200
                            `}
                        >
                            <item.icon className="h-5 w-5 mr-3" />
                            <span>{item.name}</span>
                            {item.children && (
                                <svg
                                    className={`ml-auto h-5 w-5 transform transition-transform duration-200
                                        ${expandedItems.includes(item.path) ? 'rotate-90' : ''}
                                    `}
                                    viewBox="0 0 20 20"
                                    fill="currentColor"
                                >
                                    <path
                                        fillRule="evenodd"
                                        d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
                                        clipRule="evenodd"
                                    />
                                </svg>
                            )}
                        </div>

                        {item.children && expandedItems.includes(item.path) && (
                            <div className="bg-gray-700">
                                {item.children.map((child) => (
                                    <Link
                                        key={child.path}
                                        to={child.path}
                                        className={`
                                            flex items-center pl-12 pr-4 py-2
                                            ${isActive(child.path) ? 'bg-gray-600' : 'hover:bg-gray-600'}
                                            transition-colors duration-200
                                        `}
                                    >
                                        {child.name}
                                    </Link>
                                ))}
                            </div>
                        )}
                    </div>
                ))}
            </nav>
        </div>
    );
};

export default Sidebar;