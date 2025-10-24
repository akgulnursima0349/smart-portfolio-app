import axios from 'axios';
import toast from 'react-hot-toast';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem('refreshToken');
        if (refreshToken) {
          const response = await axios.post('http://localhost:8080/api/auth/refresh', {
            refreshToken,
          });

          const { token } = response.data;
          localStorage.setItem('token', token);

          originalRequest.headers.Authorization = `Bearer ${token}`;
          return axiosInstance(originalRequest);
        }
      } catch (refreshError) {
        // Only redirect to login if it's not a public endpoint
        if (!originalRequest.url?.includes('/auth/') && !originalRequest.url?.includes('/projects') && !originalRequest.url?.includes('/blogs') && !originalRequest.url?.includes('/skills') && !originalRequest.url?.includes('/ai/')) {
          localStorage.removeItem('token');
          localStorage.removeItem('refreshToken');
          window.location.href = '/login';
        }
        return Promise.reject(refreshError);
      }
    }

    const errorMessage = error.response?.data?.message || error.message || 'An error occurred';
    toast.error(errorMessage);

    return Promise.reject(error);
  }
);

export default axiosInstance;
