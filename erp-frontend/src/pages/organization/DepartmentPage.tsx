import React, { useState } from 'react';
import axios from '@/lib/axios';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { Department } from '@/types/department';
import { Plus, Edit2, Trash2 } from 'lucide-react';

const DepartmentPage = () => {
    const queryClient = useQueryClient();
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedDept, setSelectedDept] = useState<Department | null>(null);
    const [formData, setFormData] = useState({
        name: '',
        code: '',
        parentId: '',
        sortOrder: 0,
        description: ''
    });

    const { data: departments, isLoading } = useQuery({
        queryKey: ['departments'],
        queryFn: async () => {
            const response = await axios.get('/departments');
            return response.data.data;
        }
    });

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            if (selectedDept) {
                await axios.put(`/departments/${selectedDept.id}`, formData);
            } else {
                await axios.post('/departments', formData);
            }
            await queryClient.invalidateQueries({queryKey: ['departments']});
            setIsModalOpen(false);
            resetForm();
        } catch (error) {
            console.error('Failed to save department:', error);
            alert('부서 저장에 실패했습니다.');
        }
    };

    const handleDelete = async (id: number) => {
        if (!window.confirm('정말 삭제하시겠습니까?')) return;
        try {
            await axios.delete(`/api/departments/${id}`);
            await queryClient.invalidateQueries({queryKey: ['departments']});
        } catch (error) {
            console.error('Failed to delete department:', error);
            alert('부서 삭제에 실패했습니다.');
        }
    };

    const resetForm = () => {
        setFormData({
            name: '',
            code: '',
            parentId: '',
            sortOrder: 0,
            description: ''
        });
        setSelectedDept(null);
    };

    if (isLoading) {
        return <div className="flex justify-center items-center h-full">Loading...</div>;
    }

    return (
        <div className="p-6 space-y-6">
            <div className="flex justify-between items-center">
                <h1 className="text-2xl font-bold text-gray-900">부서 관리</h1>
                <button
                    onClick={() => setIsModalOpen(true)}
                    className="flex items-center px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors"
                >
                    <Plus className="w-4 h-4 mr-2" />
                    부서 추가
                </button>
            </div>

            <div className="bg-white shadow-md rounded-lg overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">부서명</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">코드</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">상위부서</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">정렬순서</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">설명</th>
                        <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">작업</th>
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {departments?.map((dept: Department) => (
                        <tr key={dept.id}>
                            <td className="px-6 py-4 whitespace-nowrap">{dept.name}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{dept.code}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{dept.parentId || '-'}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{dept.sortOrder}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{dept.description || '-'}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                <button
                                    onClick={() => {
                                        setSelectedDept(dept);
                                        // @ts-ignore
                                        setFormData(dept);
                                        setIsModalOpen(true);
                                    }}
                                    className="text-blue-600 hover:text-blue-900 mx-2"
                                >

                                    <Edit2 className="w-4 h-4" />
                                </button>
                                <button
                                    onClick={() => handleDelete(dept.id)}
                                    className="text-red-600 hover:text-red-900"
                                >
                                    <Trash2 className="w-4 h-4" />
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            {isModalOpen && (
                <div className="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md">
                        <h2 className="text-xl font-bold mb-4">
                            {selectedDept ? '부서 수정' : '부서 추가'}
                        </h2>
                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700">부서명</label>
                                <input
                                    type="text"
                                    value={formData.name}
                                    onChange={(e) => setFormData(prev => ({ ...prev, name: e.target.value }))}
                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                    required
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">코드</label>
                                <input
                                    type="text"
                                    value={formData.code}
                                    onChange={(e) => setFormData(prev => ({ ...prev, code: e.target.value }))}
                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                    required
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">상위부서 ID</label>
                                <input
                                    type="text"
                                    value={formData.parentId}
                                    onChange={(e) => setFormData(prev => ({ ...prev, parentId: e.target.value }))}
                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">정렬순서</label>
                                <input
                                    type="number"
                                    value={formData.sortOrder}
                                    onChange={(e) => setFormData(prev => ({ ...prev, sortOrder: parseInt(e.target.value) }))}
                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">설명</label>
                                <textarea
                                    value={formData.description}
                                    onChange={(e) => setFormData(prev => ({ ...prev, description: e.target.value }))}
                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                    rows={3}
                                />
                            </div>
                            <div className="flex justify-end space-x-2">
                                <button
                                    type="button"
                                    onClick={() => {
                                        setIsModalOpen(false);
                                        resetForm();
                                    }}
                                    className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50"
                                >
                                    취소
                                </button>
                                <button
                                    type="submit"
                                    className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700"
                                >
                                    저장
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default DepartmentPage;