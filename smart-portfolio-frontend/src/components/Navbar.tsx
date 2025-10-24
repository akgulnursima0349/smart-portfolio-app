import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { LogOut, Globe } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export const Navbar = () => {
  const { t, i18n } = useTranslation();
  const { isAuthenticated, logout, user } = useAuth();

  const toggleLanguage = () => {
    const newLang = i18n.language === 'en' ? 'tr' : 'en';
    i18n.changeLanguage(newLang);
    localStorage.setItem('language', newLang);
  };

  return (
    <nav className="bg-white shadow-md">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <div className="flex items-center space-x-8">
            <Link to="/" className="text-2xl font-bold text-blue-600">
              Smart Portfolio
            </Link>
            <div className="hidden md:flex space-x-4">
              <Link to="/" className="text-gray-700 hover:text-blue-600 transition-colors">
                {t('nav.portfolio')}
              </Link>
              {isAuthenticated && (
                <Link to="/dashboard" className="text-gray-700 hover:text-blue-600 transition-colors">
                  {t('nav.dashboard')}
                </Link>
              )}
            </div>
          </div>

          <div className="flex items-center space-x-4">
            <button
              onClick={toggleLanguage}
              className="flex items-center space-x-2 text-gray-700 hover:text-blue-600 transition-colors"
            >
              <Globe size={20} />
              <span className="uppercase text-sm font-medium">{i18n.language}</span>
            </button>

            {isAuthenticated ? (
              <div className="flex items-center space-x-4">
                <span className="text-gray-700">
                  {user?.firstName || user?.username}
                </span>
                <button
                  onClick={logout}
                  className="flex items-center space-x-2 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
                >
                  <LogOut size={18} />
                  <span>{t('auth.logout')}</span>
                </button>
              </div>
            ) : (
              <Link
                to="/login"
                className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
              >
                {t('auth.login')}
              </Link>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};
