import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from './context/AuthContext';
import { ProtectedRoute } from './components/ProtectedRoute';
import { Navbar } from './components/Navbar';
import { AdminLayout } from './layouts/AdminLayout';
import { Login } from './pages/Login';
import { Register } from './pages/Register';
import { Portfolio } from './pages/Portfolio';
import { Dashboard } from './pages/Dashboard';
import { ProjectsPage } from './pages/ProjectsPage';
import { BlogsPage } from './pages/BlogsPage';
import { SkillsPage } from './pages/SkillsPage';
import { UsersPage } from './pages/UsersPage';
import AiTools from './pages/AiTools';
import { NotFound } from './pages/NotFound';
import './i18n/config';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
    },
  },
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <AuthProvider>
          <Toaster position="top-right" />
          <Routes>
            <Route
              path="/"
              element={
                <>
                  <Navbar />
                  <Portfolio />
                </>
              }
            />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            <Route
              path="/dashboard"
              element={
                <ProtectedRoute>
                  <AdminLayout>
                    <Dashboard />
                  </AdminLayout>
                </ProtectedRoute>
              }
            />
            <Route
              path="/dashboard/projects"
              element={
                <ProtectedRoute>
                  <AdminLayout>
                    <ProjectsPage />
                  </AdminLayout>
                </ProtectedRoute>
              }
            />
            <Route
              path="/dashboard/blogs"
              element={
                <ProtectedRoute>
                  <AdminLayout>
                    <BlogsPage />
                  </AdminLayout>
                </ProtectedRoute>
              }
            />
            <Route
              path="/dashboard/skills"
              element={
                <ProtectedRoute>
                  <AdminLayout>
                    <SkillsPage />
                  </AdminLayout>
                </ProtectedRoute>
              }
            />
            <Route
              path="/dashboard/users"
              element={
                <ProtectedRoute requireAdmin={true}>
                  <AdminLayout>
                    <UsersPage />
                  </AdminLayout>
                </ProtectedRoute>
              }
            />
            <Route
              path="/dashboard/ai-tools"
              element={
                <ProtectedRoute>
                  <AdminLayout>
                    <AiTools />
                  </AdminLayout>
                </ProtectedRoute>
              }
            />

            <Route path="/404" element={<NotFound />} />
            <Route path="*" element={<Navigate to="/404" replace />} />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default App;
