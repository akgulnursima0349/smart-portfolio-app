import axiosInstance from './axios';

export interface Project {
  id: number;
  title: string;
  description: string;
  imageUrl?: string;
  githubUrl?: string;
  demoUrl?: string;
  isFeatured: boolean;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface CreateProjectData {
  title: string;
  description: string;
  imageUrl?: string;
  githubUrl?: string;
  demoUrl?: string;
  isFeatured?: boolean;
}

export const projectsService = {
  getAll: async (): Promise<Project[]> => {
    const response = await axiosInstance.get('/projects');
    return response.data;
  },

  getById: async (id: number): Promise<Project> => {
    const response = await axiosInstance.get(`/projects/${id}`);
    return response.data;
  },

  create: async (data: CreateProjectData): Promise<Project> => {
    const response = await axiosInstance.post('/projects', data);
    return response.data;
  },

  update: async (id: number, data: Partial<CreateProjectData>): Promise<Project> => {
    const response = await axiosInstance.put(`/projects/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await axiosInstance.delete(`/projects/${id}`);
  },

  getFeatured: async (): Promise<Project[]> => {
    const response = await axiosInstance.get('/projects/featured');
    return response.data;
  },

  getActive: async (): Promise<Project[]> => {
    const response = await axiosInstance.get('/projects/active');
    return response.data;
  },
};
