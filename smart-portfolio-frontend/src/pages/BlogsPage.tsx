import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { blogsService, CreateBlogData } from '../api/blogs.service';
import { Card, CardBody } from '../components/Card';
import { Button } from '../components/Button';
import { Input } from '../components/Input';
import { Modal } from '../components/Modal';
import { Plus, Edit, Trash2, Eye } from 'lucide-react';
import toast from 'react-hot-toast';

export const BlogsPage = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingBlog, setEditingBlog] = useState<number | null>(null);
  const [formData, setFormData] = useState<CreateBlogData>({
    title: '',
    content: '',
    summary: '',
    imageUrl: '',
    isPublished: false,
  });

  const { data: blogs, isLoading } = useQuery({
    queryKey: ['blogs'],
    queryFn: blogsService.getAll,
  });

  const createMutation = useMutation({
    mutationFn: blogsService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['blogs'] });
      toast.success('Blog created successfully!');
      handleCloseModal();
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: Partial<CreateBlogData> }) =>
      blogsService.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['blogs'] });
      toast.success('Blog updated successfully!');
      handleCloseModal();
    },
  });

  const deleteMutation = useMutation({
    mutationFn: blogsService.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['blogs'] });
      toast.success('Blog deleted successfully!');
    },
  });

  const handleOpenModal = (blogId?: number) => {
    if (blogId) {
      const blog = blogs?.find((b) => b.id === blogId);
      if (blog) {
        setEditingBlog(blogId);
        setFormData({
          title: blog.title,
          content: blog.content,
          summary: blog.summary || '',
          imageUrl: blog.imageUrl || '',
          isPublished: blog.isPublished,
        });
      }
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingBlog(null);
    setFormData({
      title: '',
      content: '',
      summary: '',
      imageUrl: '',
      isPublished: false,
    });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (editingBlog) {
      updateMutation.mutate({ id: editingBlog, data: formData });
    } else {
      createMutation.mutate(formData);
    }
  };

  const handleDelete = (id: number) => {
    if (window.confirm(t('blogs.deleteConfirm'))) {
      deleteMutation.mutate(id);
    }
  };

  if (isLoading) {
    return <div className="text-center py-8">{t('common.loading')}</div>;
  }

  return (
    <div>
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-900">{t('blogs.title')}</h1>
        <Button onClick={() => handleOpenModal()}>
          <Plus size={20} className="mr-2" />
          {t('blogs.createBlog')}
        </Button>
      </div>

      <div className="space-y-4">
        {blogs?.map((blog) => (
          <Card key={blog.id} hover>
            <CardBody>
              <div className="flex items-start justify-between">
                <div className="flex-1">
                  <div className="flex items-center space-x-3 mb-2">
                    <h3 className="text-xl font-semibold text-gray-900">{blog.title}</h3>
                    {blog.isPublished && (
                      <span className="px-2 py-1 text-xs bg-green-100 text-green-800 rounded">
                        {t('blogs.published')}
                      </span>
                    )}
                  </div>

                  {blog.summary && (
                    <p className="text-gray-600 mb-3 line-clamp-2">{blog.summary}</p>
                  )}

                  <div className="flex items-center space-x-4">
                    <div className="flex items-center space-x-1 text-gray-500">
                      <Eye size={16} />
                      <span className="text-sm">{blog.viewCount || 0} views</span>
                    </div>

                  </div>
                </div>

                <div className="flex space-x-2 ml-4">
                  <button
                    onClick={() => handleOpenModal(blog.id)}
                    className="text-blue-600 hover:text-blue-700"
                  >
                    <Edit size={18} />
                  </button>
                  <button
                    onClick={() => handleDelete(blog.id)}
                    className="text-red-600 hover:text-red-700"
                  >
                    <Trash2 size={18} />
                  </button>
                </div>
              </div>
            </CardBody>
          </Card>
        ))}
      </div>

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={editingBlog ? t('blogs.editBlog') : t('blogs.createBlog')}
        size="xl"
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label={t('blogs.blogTitle')}
            value={formData.title}
            onChange={(e) => setFormData({ ...formData, title: e.target.value })}
            required
          />

          <Input
            label={t('blogs.summary')}
            value={formData.summary}
            onChange={(e) => setFormData({ ...formData, summary: e.target.value })}
          />

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              {t('blogs.content')}
            </label>
            <textarea
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              rows={8}
              value={formData.content}
              onChange={(e) => setFormData({ ...formData, content: e.target.value })}
              required
            />
          </div>


          <Input
            label={t('blogs.imageUrl')}
            type="url"
            value={formData.imageUrl}
            onChange={(e) => setFormData({ ...formData, imageUrl: e.target.value })}
          />

          <label className="flex items-center space-x-2">
            <input
              type="checkbox"
              checked={formData.isPublished}
              onChange={(e) => setFormData({ ...formData, isPublished: e.target.checked })}
              className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
            />
            <span className="text-sm text-gray-700">{t('blogs.published')}</span>
          </label>

          <div className="flex justify-end space-x-3 pt-4">
            <Button type="button" variant="secondary" onClick={handleCloseModal}>
              {t('common.cancel')}
            </Button>
            <Button
              type="submit"
              isLoading={createMutation.isPending || updateMutation.isPending}
            >
              {t('common.save')}
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
