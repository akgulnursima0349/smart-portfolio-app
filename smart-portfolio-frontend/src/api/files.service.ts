import axiosInstance from './axios';

export const filesService = {
  upload: async (file: File): Promise<{ url: string; fileName: string }> => {
    const formData = new FormData();
    formData.append('file', file);

    const response = await axiosInstance.post('/files/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },

  getFileUrl: async (fileName: string): Promise<string> => {
    const response = await axiosInstance.get(`/files/${fileName}/url`);
    return response.data;
  },

  checkExists: async (fileName: string): Promise<boolean> => {
    const response = await axiosInstance.get(`/files/${fileName}/exists`);
    return response.data;
  },

  delete: async (fileName: string): Promise<void> => {
    await axiosInstance.delete(`/files/${fileName}`);
  },
};
