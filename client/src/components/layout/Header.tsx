import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Bell,
  ChevronDown,
  MessageCircle,
  Search,
  Settings,
} from "lucide-react";
import { useAuthStore } from "../../stores";
import { ROUTES } from "../../constants";
import type { UserRole } from "../../types";

const Header = () => {
  const { user, logout } = useAuthStore();
  const navigate = useNavigate();

  const [notifications] = useState(0);
  const [unreadMessages] = useState(0);
  const [isUserMenuOpen, setIsUserMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate(ROUTES.LOGIN);
  };

  const getRoleBadgeColor = (role: UserRole) => {
    switch (role) {
      case "ADMIN":
        return "bg-red-100 text-red-700 border border-red-200";
      case "MANAGER":
        return "bg-blue-100 text-blue-700 border border-blue-200";
      case "STAFF":
      default:
        return "bg-green-100 text-green-700 border border-green-200";
    }
  };

  const displayRole = (role: UserRole) =>
    role.charAt(0).toUpperCase() + role.slice(1).toLowerCase();

  const userInitial = user?.fullName?.charAt(0).toUpperCase() ?? "?";

  return (
    <header className="fixed top-0 right-0 left-0 md:left-64 h-16 bg-card border-b border-border flex items-center justify-between px-6 z-30 shadow-sm text-black">
      {/* Left Section - Search */}
      <div className="hidden md:flex flex-1 max-w-md">
        <div className="flex items-center gap-2 w-full px-4 py-2 rounded-lg bg-secondary/50 border border-border hover:border-primary/50 transition-colors">
          <Search size={18} className="text-muted-foreground" />
          <input
            type="text"
            placeholder="Search devices, customers..."
            className="flex-1 bg-transparent outline-none text-sm placeholder-muted-foreground"
          />
        </div>
      </div>

      {/* Right Section - Icons & User */}
      <div className="flex items-center gap-4 ml-auto">
        {/* Notifications */}
        <button
          className="relative p-2 rounded-lg hover:bg-secondary/50 transition-colors text-muted-foreground hover:text-foreground"
          aria-label="Notifications"
        >
          <Bell size={20} />
          {notifications > 0 && (
            <span className="absolute top-1 right-1 flex items-center justify-center w-5 h-5 text-xs font-bold rounded-full bg-destructive text-destructive-foreground">
              {notifications}
            </span>
          )}
        </button>

        {/* Messages */}
        <button
          className="relative p-2 rounded-lg hover:bg-secondary/50 transition-colors text-muted-foreground hover:text-foreground"
          aria-label="Messages"
        >
          <MessageCircle size={20} />
          {unreadMessages > 0 && (
            <span className="absolute top-1 right-1 flex items-center justify-center w-5 h-5 text-xs font-bold rounded-full bg-blue-100 text-blue-700">
              {unreadMessages}
            </span>
          )}
        </button>

        {/* Settings */}
        <button
          className="p-2 rounded-lg hover:bg-secondary/50 transition-colors text-muted-foreground hover:text-foreground"
          aria-label="Settings"
        >
          <Settings size={20} />
        </button>

        {/* Divider */}
        <div className="w-px h-8 bg-border" />

        {/* User Profile */}
        {user && (
          <div className="relative">
            <button
              onClick={() => setIsUserMenuOpen((open) => !open)}
              className="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-secondary/50 transition-colors"
            >
              <div className="flex flex-col items-end">
                <span className="text-sm font-medium text-foreground">
                  {user.fullName}
                </span>
                <span
                  className={`text-xs px-2 py-0.5 rounded font-semibold ${getRoleBadgeColor(
                    user.role
                  )}`}
                >
                  {displayRole(user.role)}
                </span>
              </div>

              <div className="w-10 h-10 rounded-full bg-primary/20 flex items-center justify-center border-2 border-primary overflow-hidden">
                {user.avatarUrl ? (
                  <img
                    src={user.avatarUrl}
                    alt={user.fullName}
                    className="w-full h-full object-cover"
                  />
                ) : (
                  <span className="text-sm font-bold text-primary">
                    {userInitial}
                  </span>
                )}
              </div>

              <ChevronDown size={16} className="text-muted-foreground" />
            </button>

            {/* User Dropdown Menu */}
            {isUserMenuOpen && (
              <div className="absolute right-0 mt-2 w-56 bg-card border border-border rounded-lg shadow-lg overflow-hidden">
                <div className="px-4 py-3 border-b border-border">
                  <p className="text-sm font-medium">{user.fullName}</p>
                  <p className="text-xs text-muted-foreground">{user.email}</p>
                </div>
                <button className="w-full text-left px-4 py-2 text-sm hover:bg-secondary/50 transition-colors">
                  Profile Settings
                </button>
                <button className="w-full text-left px-4 py-2 text-sm hover:bg-secondary/50 transition-colors border-t border-border">
                  Help &amp; Support
                </button>
                <button
                  className="w-full text-left px-4 py-2 text-sm text-destructive hover:bg-destructive/10 transition-colors border-t border-border"
                  onClick={handleLogout}
                >
                  Logout
                </button>
              </div>
            )}
          </div>
        )}
      </div>
    </header>
  );
};

export default Header;
