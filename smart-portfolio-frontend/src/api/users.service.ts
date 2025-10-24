import axiosInstance from './axios';

export interface User {
  id: number;
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  roles: string[];
  createdAt: string;
  updatedAt: string;
}

export interface UpdateUserData {
  username?: string;
  email?: string;
  firstName?: string;
  lastName?: string;
}

export interface UserRole {
  id: number;
  name: string;
}

export const usersService = {
  getAll: async (): Promise<User[]> => {
    const response = await axiosInstance.get('/users');
    return response.data;
  },

  getById: async (id: number): Promise<User> => {
    const response = await axiosInstance.get(`/users/${id}`);
    return response.data;
  },

  update: async (id: number, data: UpdateUserData): Promise<User> => {
    const response = await axiosInstance.put(`/users/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await axiosInstance.delete(`/users/${id}`);
  },

  getRoles: async (id: number): Promise<UserRole[]> => {
    const response = await axiosInstance.get(`/users/${id}/roles`);
    return response.data;
  },

  updateRoles: async (id: number, roles: string[]): Promise<UserRole[]> => {
    const response = await axiosInstance.put(`/users/${id}/roles`, { roles });
    return response.data;
  },
};
