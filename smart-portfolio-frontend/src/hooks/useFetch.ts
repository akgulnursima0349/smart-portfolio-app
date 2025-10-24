import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'react-hot-toast';

export const useFetch = <T>(
  queryKey: string[],
  queryFn: () => Promise<T>,
  options?: {
    enabled?: boolean;
    staleTime?: number;
    cacheTime?: number;
  }
) => {
  return useQuery({
    queryKey,
    queryFn,
    enabled: options?.enabled ?? true,
    staleTime: options?.staleTime ?? 5 * 60 * 1000, // 5 minutes
    cacheTime: options?.cacheTime ?? 10 * 60 * 1000, // 10 minutes
  });
};

export const useCreate = <TData, TVariables>(
  mutationFn: (variables: TVariables) => Promise<TData>,
  queryKey: string[],
  options?: {
    onSuccess?: (data: TData) => void;
    onError?: (error: any) => void;
    successMessage?: string;
  }
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn,
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey });
      toast.success(options?.successMessage || 'Created successfully!');
      options?.onSuccess?.(data);
    },
    onError: (error) => {
      toast.error(error.message || 'An error occurred');
      options?.onError?.(error);
    },
  });
};

export const useUpdate = <TData, TVariables>(
  mutationFn: (variables: TVariables) => Promise<TData>,
  queryKey: string[],
  options?: {
    onSuccess?: (data: TData) => void;
    onError?: (error: any) => void;
    successMessage?: string;
  }
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn,
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey });
      toast.success(options?.successMessage || 'Updated successfully!');
      options?.onSuccess?.(data);
    },
    onError: (error) => {
      toast.error(error.message || 'An error occurred');
      options?.onError?.(error);
    },
  });
};

export const useDelete = <TVariables>(
  mutationFn: (variables: TVariables) => Promise<void>,
  queryKey: string[],
  options?: {
    onSuccess?: () => void;
    onError?: (error: any) => void;
    successMessage?: string;
  }
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey });
      toast.success(options?.successMessage || 'Deleted successfully!');
      options?.onSuccess?.();
    },
    onError: (error) => {
      toast.error(error.message || 'An error occurred');
      options?.onError?.(error);
    },
  });
};
