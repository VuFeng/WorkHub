import { Link, useLocation } from 'react-router-dom';
import { ROUTES } from '../../constants';
import { useAuthStore, useUIStore } from '../../stores';
import { UserRole } from '../../types';

interface NavItem {
  path: string;
  label: string;
  icon?: React.ReactNode;
  roles?: UserRole[];
}

const navItems: NavItem[] = [
  { path: ROUTES.DASHBOARD, label: 'Dashboard' },
  { path: ROUTES.COMPANIES, label: 'Companies', roles: [UserRole.ADMIN] },
  { path: ROUTES.USERS, label: 'Users', roles: [UserRole.ADMIN, UserRole.MANAGER] },
  { path: ROUTES.JOBS, label: 'Jobs' },
  { path: ROUTES.TASKS, label: 'Tasks' },
];

const Sidebar = () => {
  const location = useLocation();
  const { user, hasAnyRole } = useAuthStore();
  const { sidebarOpen, setSidebarOpen } = useUIStore();

  // Filter nav items based on user role
  const filteredNavItems = navItems.filter((item) => {
    if (!item.roles) return true;
    return user && hasAnyRole(item.roles);
  });

  return (
    <>
      {/* Mobile overlay */}
      {sidebarOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-40 lg:hidden"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      {/* Sidebar */}
      <aside
        className={`
          fixed top-0 left-0 h-full bg-white border-r border-gray-200 z-50
          transform transition-transform duration-300 ease-in-out
          ${sidebarOpen ? 'translate-x-0' : '-translate-x-full'}
          lg:translate-x-0 lg:static lg:z-auto
          w-64
        `}
      >
        <div className="h-full flex flex-col">
          {/* Sidebar header */}
          <div className="px-6 py-4 border-b border-gray-200 flex items-center justify-between">
            <h2 className="text-lg font-semibold text-gray-900">Menu</h2>
            <button
              onClick={() => setSidebarOpen(false)}
              className="lg:hidden text-gray-400 hover:text-gray-500"
            >
              <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          {/* Navigation */}
          <nav className="flex-1 px-4 py-4 space-y-1">
            {filteredNavItems.map((item) => {
              const isActive = location.pathname === item.path;
              return (
                <Link
                  key={item.path}
                  to={item.path}
                  className={`
                    flex items-center px-4 py-2 rounded-lg transition-colors
                    ${
                      isActive
                        ? 'bg-blue-50 text-blue-700 font-medium'
                        : 'text-gray-700 hover:bg-gray-50'
                    }
                  `}
                  onClick={() => setSidebarOpen(false)}
                >
                  {item.icon && <span className="mr-3">{item.icon}</span>}
                  <span>{item.label}</span>
                </Link>
              );
            })}
          </nav>
        </div>
      </aside>
    </>
  );
};

export default Sidebar;

