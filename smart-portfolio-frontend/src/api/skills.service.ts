import axiosInstance from './axios';

export interface Skill {
  id: number;
  name: string;
  level: number;
  category?: string;
  iconUrl?: string;
  createdAt: string;
}

export interface CreateSkillData {
  name: string;
  level: number;
  category: string;
  iconUrl?: string;
}

export const skillsService = {
  getAll: async (): Promise<Skill[]> => {
    const response = await axiosInstance.get('/skills');
    return response.data;
  },

  getById: async (id: number): Promise<Skill> => {
    const response = await axiosInstance.get(`/skills/${id}`);
    return response.data;
  },

  create: async (data: CreateSkillData): Promise<Skill> => {
    const response = await axiosInstance.post('/skills', data);
    return response.data;
  },

  update: async (id: number, data: Partial<CreateSkillData>): Promise<Skill> => {
    const response = await axiosInstance.put(`/skills/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await axiosInstance.delete(`/skills/${id}`);
  },

  getByLevel: async (level: number): Promise<Skill[]> => {
    const response = await axiosInstance.get(`/skills/level/${level}`);
    return response.data;
  },
};
