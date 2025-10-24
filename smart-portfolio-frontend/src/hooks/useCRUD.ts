import { useFetch, useCreate, useUpdate, useDelete } from './useFetch';

export const useCRUD = <T, TCreate, TUpdate>(
  entityName: string,
  service: {
    getAll: () => Promise<T[]>;
    getById: (id: number) => Promise<T>;
    create: (data: TCreate) => Promise<T>;
    update: (id: number, data: TUpdate) => Promise<T>;
    delete: (id: number) => Promise<void>;
  }
) => {
  const queryKey = [entityName];

  const {
    data: items,
    isLoading,
    error,
    refetch,
  } = useFetch(queryKey, service.getAll);

  const createMutation = useCreate(
    service.create,
    queryKey,
    {
      successMessage: `${entityName} created successfully!`,
    }
  );

  const updateMutation = useUpdate(
    ({ id, data }: { id: number; data: TUpdate }) => service.update(id, data),
    queryKey,
    {
      successMessage: `${entityName} updated successfully!`,
    }
  );

  const deleteMutation = useDelete(
    (id: number) => service.delete(id),
    queryKey,
    {
      successMessage: `${entityName} deleted successfully!`,
    }
  );

  return {
    items: items || [],
    isLoading,
    error,
    refetch,
    create: createMutation.mutate,
    update: updateMutation.mutate,
    delete: deleteMutation.mutate,
    isCreating: createMutation.isPending,
    isUpdating: updateMutation.isPending,
    isDeleting: deleteMutation.isPending,
  };
};
