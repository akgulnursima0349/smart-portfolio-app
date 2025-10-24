import axiosInstance from './axios';

export interface Blog {
  id: number;
  title: string;
  content: string;
  summary?: string;
  imageUrl?: string;
  isPublished: boolean;
  viewCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface CreateBlogData {
  title: string;
  content: string;
  summary?: string;
  imageUrl?: string;
  isPublished?: boolean;
}

export const blogsService = {
  getAll: async (): Promise<Blog[]> => {
    const response = await axiosInstance.get('/blogs/admin/all');
    return response.data;
  },

  getById: async (id: number): Promise<Blog> => {
    const response = await axiosInstance.get(`/blogs/${id}`);
    return response.data;
  },

  create: async (data: CreateBlogData): Promise<Blog> => {
    const response = await axiosInstance.post('/blogs', data);
    return response.data;
  },

  update: async (id: number, data: Partial<CreateBlogData>): Promise<Blog> => {
    const response = await axiosInstance.put(`/blogs/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await axiosInstance.delete(`/blogs/${id}`);
  },

  search: async (query: string): Promise<Blog[]> => {
    const response = await axiosInstance.get('/blogs/search', {
      params: { q: query },
    });
    return response.data;
  },

  getViews: async (id: number): Promise<number> => {
    const response = await axiosInstance.get(`/blogs/${id}/views`);
    return response.data;
  },

  incrementViews: async (id: number): Promise<void> => {
    await axiosInstance.post(`/blogs/${id}/views`);
  },
};
