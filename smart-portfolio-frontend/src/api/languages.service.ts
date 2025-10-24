import axiosInstance from './axios';

export interface Language {
  id: number;
  name: string;
  code: string;
  nativeName?: string;
  flagUrl?: string;
  createdAt: string;
}

export interface CreateLanguageData {
  name: string;
  code: string;
  nativeName?: string;
  flagUrl?: string;
}

export const languagesService = {
  getAll: async (): Promise<Language[]> => {
    const response = await axiosInstance.get('/languages');
    return response.data;
  },

  getById: async (id: number): Promise<Language> => {
    const response = await axiosInstance.get(`/languages/${id}`);
    return response.data;
  },

  create: async (data: CreateLanguageData): Promise<Language> => {
    const response = await axiosInstance.post('/languages', data);
    return response.data;
  },

  update: async (id: number, data: Partial<CreateLanguageData>): Promise<Language> => {
    const response = await axiosInstance.put(`/languages/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await axiosInstance.delete(`/languages/${id}`);
  },

  getByCode: async (code: string): Promise<Language> => {
    const response = await axiosInstance.get(`/languages/code/${code}`);
    return response.data;
  },
};
