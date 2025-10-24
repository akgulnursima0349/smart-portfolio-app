import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { skillsService, CreateSkillData } from '../api/skills.service';
import { Card, CardBody } from '../components/Card';
import { Button } from '../components/Button';
import { Input } from '../components/Input';
import { Modal } from '../components/Modal';
import { Plus, Edit, Trash2 } from 'lucide-react';
import toast from 'react-hot-toast';

export const SkillsPage = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingSkill, setEditingSkill] = useState<number | null>(null);
  const [formData, setFormData] = useState<CreateSkillData>({
    name: '',
    level: 5,
    category: '',
    iconUrl: '',
  });

  const { data: skills, isLoading } = useQuery({
    queryKey: ['skills'],
    queryFn: skillsService.getAll,
  });

  const createMutation = useMutation({
    mutationFn: skillsService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['skills'] });
      toast.success('Skill created successfully!');
      handleCloseModal();
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: Partial<CreateSkillData> }) =>
      skillsService.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['skills'] });
      toast.success('Skill updated successfully!');
      handleCloseModal();
    },
  });

  const deleteMutation = useMutation({
    mutationFn: skillsService.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['skills'] });
      toast.success('Skill deleted successfully!');
    },
  });

  const handleOpenModal = (skillId?: number) => {
    if (skillId) {
      const skill = skills?.find((s) => s.id === skillId);
      if (skill) {
        setEditingSkill(skillId);
        setFormData({
          name: skill.name,
          level: skill.level,
          category: skill.category,
          iconUrl: skill.iconUrl || '',
        });
      }
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingSkill(null);
    setFormData({
      name: '',
      level: 5,
      category: '',
      iconUrl: '',
    });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (editingSkill) {
      updateMutation.mutate({ id: editingSkill, data: formData });
    } else {
      createMutation.mutate(formData);
    }
  };

  const handleDelete = (id: number) => {
    if (window.confirm(t('skills.deleteConfirm'))) {
      deleteMutation.mutate(id);
    }
  };

  const groupedSkills = skills?.reduce((acc, skill) => {
    if (!acc[skill.category]) {
      acc[skill.category] = [];
    }
    acc[skill.category].push(skill);
    return acc;
  }, {} as Record<string, typeof skills>);

  if (isLoading) {
    return <div className="text-center py-8">{t('common.loading')}</div>;
  }

  return (
    <div>
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-900">{t('skills.title')}</h1>
        <Button onClick={() => handleOpenModal()}>
          <Plus size={20} className="mr-2" />
          {t('skills.createSkill')}
        </Button>
      </div>

      <div className="space-y-8">
        {groupedSkills && Object.entries(groupedSkills).map(([category, categorySkills]) => (
          <div key={category}>
            <h2 className="text-2xl font-semibold text-gray-900 mb-4">{category}</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {categorySkills.map((skill) => (
                <Card key={skill.id} hover>
                  <CardBody>
                    <div className="flex items-start justify-between mb-3">
                      <div className="flex items-center space-x-3">
                        {skill.iconUrl && (
                          <img
                            src={skill.iconUrl}
                            alt={skill.name}
                            className="w-8 h-8 object-contain"
                          />
                        )}
                        <h3 className="text-lg font-semibold text-gray-900">{skill.name}</h3>
                      </div>
                      <div className="flex space-x-2">
                        <button
                          onClick={() => handleOpenModal(skill.id)}
                          className="text-blue-600 hover:text-blue-700"
                        >
                          <Edit size={16} />
                        </button>
                        <button
                          onClick={() => handleDelete(skill.id)}
                          className="text-red-600 hover:text-red-700"
                        >
                          <Trash2 size={16} />
                        </button>
                      </div>
                    </div>

                    <div className="space-y-2">
                      <div className="flex justify-between text-sm text-gray-600">
                        <span>{t('skills.level')}</span>
                        <span>{skill.level}/10</span>
                      </div>
                      <div className="w-full bg-gray-200 rounded-full h-2">
                        <div
                          className="bg-blue-600 h-2 rounded-full transition-all"
                          style={{ width: `${(skill.level / 10) * 100}%` }}
                        />
                      </div>
                    </div>
                  </CardBody>
                </Card>
              ))}
            </div>
          </div>
        ))}
      </div>

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={editingSkill ? t('skills.editSkill') : t('skills.createSkill')}
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label={t('skills.skillName')}
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            required
          />

          <Input
            label={t('skills.category')}
            value={formData.category}
            onChange={(e) => setFormData({ ...formData, category: e.target.value })}
            required
          />

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              {t('skills.level')} ({formData.level}/10)
            </label>
            <input
              type="range"
              min="1"
              max="10"
              value={formData.level}
              onChange={(e) => setFormData({ ...formData, level: Number(e.target.value) })}
              className="w-full"
            />
          </div>

          <Input
            label={t('skills.iconUrl')}
            type="url"
            value={formData.iconUrl}
            onChange={(e) => setFormData({ ...formData, iconUrl: e.target.value })}
          />

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
