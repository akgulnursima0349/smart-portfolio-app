import { useTranslation } from 'react-i18next';
import { useQuery } from '@tanstack/react-query';
import { projectsService } from '../api/projects.service';
import { blogsService } from '../api/blogs.service';
import { skillsService } from '../api/skills.service';
import { Card, CardBody } from '../components/Card';
import { ExternalLink, Github, Calendar, Eye } from 'lucide-react';

export const Portfolio = () => {
  const { t } = useTranslation();

  const { data: projects } = useQuery({
    queryKey: ['featured-projects'],
    queryFn: projectsService.getFeatured,
  });

  const { data: blogs } = useQuery({
    queryKey: ['blogs'],
    queryFn: blogsService.getAll,
  });

  const { data: skills } = useQuery({
    queryKey: ['skills'],
    queryFn: skillsService.getAll,
  });

  const groupedSkills = skills?.reduce((acc, skill) => {
    if (!acc[skill.category]) {
      acc[skill.category] = [];
    }
    acc[skill.category].push(skill);
    return acc;
  }, {} as Record<string, typeof skills>);

  const publishedBlogs = blogs?.filter((blog) => blog.published);

  return (
    <div className="min-h-screen bg-gray-50">
      <section className="bg-gradient-to-r from-blue-600 to-blue-800 text-white py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <h1 className="text-5xl font-bold mb-4">Smart Portfolio</h1>
            <p className="text-xl text-blue-100 mb-8">
              Full Stack Developer & Creative Problem Solver
            </p>
            <div className="flex justify-center space-x-4">
              <a
                href="#projects"
                className="px-6 py-3 bg-white text-blue-600 rounded-lg font-medium hover:bg-blue-50 transition-colors"
              >
                View Projects
              </a>
              <a
                href="#contact"
                className="px-6 py-3 bg-blue-700 text-white rounded-lg font-medium hover:bg-blue-600 transition-colors"
              >
                Get in Touch
              </a>
            </div>
          </div>
        </div>
      </section>

      <section id="projects" className="py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-4xl font-bold text-gray-900 mb-4">
              {t('portfolio.featuredProjects')}
            </h2>
            <p className="text-lg text-gray-600">Check out some of my recent work</p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {projects?.map((project) => (
              <Card key={project.id} hover className="overflow-hidden">
                {project.imageUrl && (
                  <img
                    src={project.imageUrl}
                    alt={project.title}
                    className="w-full h-48 object-cover"
                  />
                )}
                <CardBody>
                  <h3 className="text-xl font-semibold text-gray-900 mb-3">{project.title}</h3>
                  <p className="text-gray-600 mb-4">{project.description}</p>

                  <div className="flex flex-wrap gap-2 mb-4">
                    {project.technologies.map((tech, index) => (
                      <span
                        key={index}
                        className="px-3 py-1 text-sm bg-blue-100 text-blue-800 rounded-full"
                      >
                        {tech}
                      </span>
                    ))}
                  </div>

                  <div className="flex items-center space-x-4">
                    {project.githubUrl && (
                      <a
                        href={project.githubUrl}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="flex items-center space-x-1 text-gray-600 hover:text-gray-900 transition-colors"
                      >
                        <Github size={18} />
                        <span className="text-sm">Code</span>
                      </a>
                    )}
                    {project.liveUrl && (
                      <a
                        href={project.liveUrl}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="flex items-center space-x-1 text-gray-600 hover:text-gray-900 transition-colors"
                      >
                        <ExternalLink size={18} />
                        <span className="text-sm">Live Demo</span>
                      </a>
                    )}
                  </div>
                </CardBody>
              </Card>
            ))}
          </div>
        </div>
      </section>

      <section className="bg-white py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-4xl font-bold text-gray-900 mb-4">{t('portfolio.mySkills')}</h2>
            <p className="text-lg text-gray-600">Technologies and tools I work with</p>
          </div>

          <div className="space-y-12">
            {groupedSkills &&
              Object.entries(groupedSkills).map(([category, categorySkills]) => (
                <div key={category}>
                  <h3 className="text-2xl font-semibold text-gray-900 mb-6">{category}</h3>
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {categorySkills.map((skill) => (
                      <div key={skill.id} className="bg-gray-50 p-4 rounded-lg">
                        <div className="flex items-center space-x-3 mb-3">
                          {skill.iconUrl && (
                            <img
                              src={skill.iconUrl}
                              alt={skill.name}
                              className="w-8 h-8 object-contain"
                            />
                          )}
                          <h4 className="text-lg font-medium text-gray-900">{skill.name}</h4>
                        </div>
                        <div className="w-full bg-gray-200 rounded-full h-2">
                          <div
                            className="bg-blue-600 h-2 rounded-full transition-all"
                            style={{ width: `${(skill.level / 10) * 100}%` }}
                          />
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              ))}
          </div>
        </div>
      </section>

      <section className="py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-4xl font-bold text-gray-900 mb-4">{t('portfolio.latestBlogs')}</h2>
            <p className="text-lg text-gray-600">Read my latest articles and tutorials</p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {publishedBlogs?.slice(0, 6).map((blog) => (
              <Card key={blog.id} hover className="overflow-hidden">
                {blog.imageUrl && (
                  <img
                    src={blog.imageUrl}
                    alt={blog.title}
                    className="w-full h-48 object-cover"
                  />
                )}
                <CardBody>
                  <h3 className="text-xl font-semibold text-gray-900 mb-3">{blog.title}</h3>
                  {blog.excerpt && <p className="text-gray-600 mb-4">{blog.excerpt}</p>}

                  <div className="flex flex-wrap gap-2 mb-4">
                    {blog.tags.slice(0, 3).map((tag, index) => (
                      <span
                        key={index}
                        className="px-3 py-1 text-sm bg-gray-100 text-gray-700 rounded-full"
                      >
                        #{tag}
                      </span>
                    ))}
                  </div>

                  <div className="flex items-center justify-between text-sm text-gray-500">
                    <div className="flex items-center space-x-1">
                      <Calendar size={16} />
                      <span>{new Date(blog.createdAt).toLocaleDateString()}</span>
                    </div>
                    <div className="flex items-center space-x-1">
                      <Eye size={16} />
                      <span>{blog.views} views</span>
                    </div>
                  </div>
                </CardBody>
              </Card>
            ))}
          </div>
        </div>
      </section>

      <footer className="bg-gray-900 text-white py-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <p className="text-lg mb-4">Let's work together!</p>
          <p className="text-gray-400">
            &copy; {new Date().getFullYear()} Smart Portfolio. All rights reserved.
          </p>
        </div>
      </footer>
    </div>
  );
};
