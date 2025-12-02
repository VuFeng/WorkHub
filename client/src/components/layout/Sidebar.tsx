import { useState, type ReactNode } from "react";
import { Link, useLocation } from "react-router-dom";
import {
  BarChart3,
  LayoutGrid,
  LogOut,
  MessageCircle,
  Users,
  Wrench,
  X,
  Menu,
} from "lucide-react";
import { ROUTES } from "../../constants";
import { useAuthStore } from "../../stores";
import { UserRole } from "../../types";

interface MenuItemProps {
  icon: ReactNode;
  label: string;
  href: string;
  badge?: number;
  isActive: boolean;
}

interface SidebarItem {
  icon: ReactNode;
  label: string;
  href: string;
  badge?: number;
  roles?: UserRole[];
}

interface SidebarSection {
  section: string;
  items: SidebarItem[];
}

const MenuItem = ({ icon, label, href, badge, isActive }: MenuItemProps) => {
  return (
    <Link to={href}>
      <div
        className={`flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 group ${
          isActive
            ? "bg-primary text-primary-foreground"
            : "text-foreground hover:bg-secondary/50"
        }`}
      >
        <span
          className={`flex shrink-0 ${
            isActive ? "text-primary-foreground" : "text-muted-foreground"
          }`}
        >
          {icon}
        </span>
        <span className="flex-1 text-sm font-medium">{label}</span>
        {badge && badge > 0 && (
          <span className="flex items-center justify-center w-6 h-6 text-xs font-bold rounded-full bg-primary text-primary-foreground">
            {badge}
          </span>
        )}
      </div>
    </Link>
  );
};

const Sidebar = () => {
  const [isOpen, setIsOpen] = useState(false);
  const location = useLocation();
  const { user, hasAnyRole } = useAuthStore();

  const menuSections: SidebarSection[] = [
    {
      section: "Main",
      items: [
        {
          icon: <LayoutGrid size={20} />,
          label: "Dashboard",
          href: ROUTES.DASHBOARD,
          badge: 0,
        },
        {
          icon: <BarChart3 size={20} />,
          label: "Jobs",
          href: ROUTES.JOBS,
          badge: 0,
        },
        {
          icon: <MessageCircle size={20} />,
          label: "Tasks",
          href: ROUTES.TASKS,
          badge: 0,
        },
      ],
    },
    {
      section: "Management",
      items: [
        {
          icon: <Wrench size={20} />,
          label: "Companies",
          href: ROUTES.COMPANIES,
          badge: 0,
          roles: [UserRole.ADMIN],
        },
        {
          icon: <Users size={20} />,
          label: "Users",
          href: ROUTES.USERS,
          badge: 0,
          roles: [UserRole.ADMIN, UserRole.MANAGER],
        },
      ],
    },
    {
      section: "Communication",
      items: [
        {
          icon: <MessageCircle size={20} />,
          label: "Chat",
          href: "/chat",
          badge: 3,
        },
      ],
    },
  ];

  const filteredSections: SidebarSection[] = menuSections.map((section) => ({
    ...section,
    items: section.items.filter((item: SidebarItem) => {
      if (!item.roles) return true;
      if (!user) return false;
      return hasAnyRole(item.roles);
    }),
  }));

  return (
    <>
      {/* Mobile Toggle (open button when sidebar is closed) */}
      {!isOpen && (
        <button
          onClick={() => setIsOpen(true)}
          className="fixed cursor-pointer top-4 left-4 z-50 p-2 md:hidden rounded-lg bg-primary text-primary-foreground shadow-md"
          aria-label="Open sidebar"
        >
          <Menu size={24} />
        </button>
      )}

      {/* Sidebar */}
      <aside
        className={`fixed left-0 top-0 h-screen w-64 bg-card text-foreground border-r border-border transition-transform duration-300 z-40 md:translate-x-0 ${
          isOpen ? "translate-x-0" : "-translate-x-full md:translate-x-0"
        }`}
      >
        {/* Logo Section */}
        <div className="flex items-center justify-between px-6 py-6 border-b border-border">
          <div className="flex items-center gap-2">
            <div className="w-10 h-10 rounded-lg bg-primary flex items-center justify-center">
              <Wrench size={24} className="text-primary-foreground" />
            </div>
            <div>
              <h1 className="font-bold text-lg text-foreground">WorkHub</h1>
              <p className="text-xs text-muted-foreground">Workspace Manager</p>
            </div>
          </div>

          {/* Close button (mobile only) */}
          <button
            onClick={() => setIsOpen(false)}
            className="md:hidden p-2 rounded-lg hover:bg-secondary/60 text-foreground cursor-pointer"
            aria-label="Close sidebar"
          >
            <X size={20} />
          </button>
        </div>

        {/* Menu Sections */}
        <nav className="flex-1 overflow-y-auto px-3 py-6 space-y-8">
          {filteredSections.map((section) => (
            <div key={section.section}>
              <h2 className="px-4 py-2 text-xs font-semibold text-muted-foreground uppercase tracking-wider">
                {section.section}
              </h2>
              <div className="space-y-2">
                {section.items.map((item) => (
                  <MenuItem
                    key={item.href}
                    icon={item.icon}
                    label={item.label}
                    href={item.href}
                    badge={item.badge}
                    isActive={location.pathname === item.href}
                  />
                ))}
              </div>
            </div>
          ))}
        </nav>

        {/* Bottom Section */}
        <div className="border-t border-border p-3">
          <button
            className="flex items-center gap-3 w-full px-4 py-3 rounded-lg text-foreground hover:bg-secondary/50 transition-all duration-200"
            onClick={() => {
              // optional: integrate with authStore.logout if desired
            }}
          >
            <LogOut size={20} className="text-destructive" />
            <span className="text-sm font-medium">Logout</span>
          </button>
        </div>
      </aside>

      {/* Overlay */}
      {isOpen && (
        <div
          className="fixed inset-0 bg-black/50 z-30 md:hidden"
          onClick={() => setIsOpen(false)}
        />
      )}
    </>
  );
};

export default Sidebar;
