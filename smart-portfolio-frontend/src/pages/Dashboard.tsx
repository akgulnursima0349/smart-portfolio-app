import { useTranslation } from 'react-i18next';
import { useQuery } from '@tanstack/react-query';
import { projectsService } from '../api/projects.service';
import { blogsService } from '../api/blogs.service';
import { skillsService } from '../api/skills.service';
import { Card, CardBody, CardHeader } from '../components/Card';
import { FolderGit2, BookOpen, Lightbulb, TrendingUp } from 'lucide-react';

export const Dashboard = () => {
  const { t } = useTranslation();

  const { data: projects } = useQuery({
    queryKey: ['projects'],
    queryFn: projectsService.getAll,
  });

  const { data: blogs } = useQuery({
    queryKey: ['blogs'],
    queryFn: blogsService.getAll,
  });

  const { data: skills } = useQuery({
    queryKey: ['skills'],
    queryFn: skillsService.getAll,
  });

  const stats = [
    {
      name: t('nav.projects'),
      value: projects?.length || 0,
      icon: FolderGit2,
      color: 'bg-blue-500',
    },
    {
      name: t('nav.blogs'),
      value: blogs?.length || 0,
      icon: BookOpen,
      color: 'bg-green-500',
    },
    {
      name: t('nav.skills'),
      value: skills?.length || 0,
      icon: Lightbulb,
      color: 'bg-yellow-500',
    },
    {
      name: 'Total Views',
      value: blogs?.reduce((acc, blog) => acc + (blog.viewCount || 0), 0) || 0,
      icon: TrendingUp,
      color: 'bg-purple-500',
    },
  ];

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">{t('nav.dashboard')}</h1>
        <p className="mt-2 text-gray-600">Welcome back! Here's your portfolio overview.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {stats.map((stat) => {
          const Icon = stat.icon;
          return (
            <Card key={stat.name} hover>
              <CardBody className="flex items-center space-x-4">
                <div className={`${stat.color} p-3 rounded-lg text-white`}>
                  <Icon size={24} />
                </div>
                <div>
                  <p className="text-sm text-gray-600">{stat.name}</p>
                  <p className="text-2xl font-bold text-gray-900">{stat.value}</p>
                </div>
              </CardBody>
            </Card>
          );
        })}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <h2 className="text-xl font-semibold text-gray-900">Recent Projects</h2>
          </CardHeader>
          <CardBody>
            {projects && projects.length > 0 ? (
              <div className="space-y-3">
                {projects.slice(0, 5).map((project) => (
                  <div key={project.id} className="flex items-center justify-between py-2 border-b last:border-b-0">
                    <div>
                      <p className="font-medium text-gray-900">{project.title}</p>
                      <p className="text-sm text-gray-500">{project.description?.substring(0, 50)}...</p>
                    </div>
                    {project.isFeatured && (
                      <span className="px-2 py-1 text-xs bg-blue-100 text-blue-800 rounded">Featured</span>
                    )}
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-gray-500 text-center py-4">No projects yet</p>
            )}
          </CardBody>
        </Card>

        <Card>
          <CardHeader>
            <h2 className="text-xl font-semibold text-gray-900">Recent Blogs</h2>
          </CardHeader>
          <CardBody>
            {blogs && blogs.length > 0 ? (
              <div className="space-y-3">
                {blogs.slice(0, 5).map((blog) => (
                  <div key={blog.id} className="flex items-center justify-between py-2 border-b last:border-b-0">
                    <div>
                      <p className="font-medium text-gray-900">{blog.title}</p>
                      <p className="text-sm text-gray-500">{blog.viewCount || 0} views</p>
                    </div>
                    {blog.isPublished && (
                      <span className="px-2 py-1 text-xs bg-green-100 text-green-800 rounded">Published</span>
                    )}
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-gray-500 text-center py-4">No blogs yet</p>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
};
