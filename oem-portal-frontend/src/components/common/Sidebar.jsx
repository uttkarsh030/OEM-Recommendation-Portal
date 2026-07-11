import { NavLink, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { getInitials } from '../../utils/helpers';
import {
  FiHome, FiUsers, FiPackage, FiFileText,
  FiList, FiShield, FiLogOut,
  FiChevronLeft, FiChevronRight
} from 'react-icons/fi';

const navConfig = {
  ADMIN: [
    { label: 'Dashboard',       path: '/admin/dashboard',       icon: FiHome },
    { label: 'Departments',     path: '/admin/departments',     icon: FiShield },
    { label: 'Bankers',         path: '/admin/bankers',         icon: FiUsers },
    { label: 'Vendors',         path: '/admin/vendors',         icon: FiPackage },
    { label: 'Recommendations', path: '/admin/recommendations', icon: FiFileText },
    { label: 'Audit Logs',      path: '/admin/audit-logs',      icon: FiList },
  ],
  VENDOR: [
    { label: 'Dashboard',       path: '/vendor/dashboard',       icon: FiHome },
    { label: 'Recommendations', path: '/vendor/recommendations', icon: FiFileText },
  ],
  DEPARTMENT_HEAD: [
    { label: 'Dashboard',       path: '/dh/dashboard',       icon: FiHome },
    { label: 'Recommendations', path: '/dh/recommendations', icon: FiFileText },
    { label: 'My Bankers',      path: '/dh/bankers',         icon: FiUsers },
  ],
  BANKER: [
    { label: 'Dashboard',       path: '/banker/dashboard',       icon: FiHome },
    { label: 'Recommendations', path: '/banker/recommendations', icon: FiFileText },
  ],
};

const roleLabel = {
  ADMIN:           'Administrator',
  VENDOR:          'Vendor',
  DEPARTMENT_HEAD: 'Department Head',
  BANKER:          'Banker',
};

const Sidebar = () => {
  const { user, logout } = useAuth();
  const navigate         = useNavigate();
  const [collapsed, setCollapsed] = useState(false);
  const navItems = navConfig[user?.role] || [];

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <aside className={`
      ${collapsed ? 'w-16' : 'w-60'} shrink-0
      flex flex-col
      bg-slate-900
      border-r border-slate-800
      transition-all duration-300 ease-in-out
    `}>

      {/* Logo */}
      <div className={`
        flex items-center h-16 px-4 border-b border-slate-800
        ${collapsed ? 'justify-center' : 'justify-between'}
      `}>
        {!collapsed && (
          <div className="flex items-center gap-2.5">
            <div className="w-8 h-8 bg-brand-500 rounded-lg
                            flex items-center justify-center shrink-0
                            shadow-[0_0_0_3px_rgba(12,142,231,0.2)]">
              <span className="text-white text-sm font-bold">O</span>
            </div>
            <div>
              <p className="text-white text-sm font-semibold leading-none tracking-tight">
                OEM Portal
              </p>
              <p className="text-slate-400 text-[11px] mt-0.5 font-medium">
                {roleLabel[user?.role]}
              </p>
            </div>
          </div>
        )}
        <button
          onClick={() => setCollapsed(!collapsed)}
          className="w-7 h-7 flex items-center justify-center rounded-md
                     text-slate-500 hover:text-white hover:bg-slate-800
                     transition-all duration-150"
        >
          {collapsed ? <FiChevronRight size={15} /> : <FiChevronLeft size={15} />}
        </button>
      </div>

      {/* Navigation */}
      <nav className="flex-1 py-4 px-2 space-y-0.5 overflow-y-auto">
        {!collapsed && (
          <p className="section-title text-slate-600 px-3 mb-3">
            Menu
          </p>
        )}
        {navItems.map((item) => {
          const Icon = item.icon;
          return (
            <NavLink
              key={item.path}
              to={item.path}
              title={collapsed ? item.label : undefined}
              className={({ isActive }) =>
                isActive ? 'nav-item-active' : 'nav-item-inactive'
              }
            >
              <Icon size={17} className="shrink-0" />
              {!collapsed && (
                <span className="truncate">{item.label}</span>
              )}
            </NavLink>
          );
        })}
      </nav>

      {/* User footer */}
      <div className="p-3 border-t border-slate-800">
        {!collapsed ? (
          <div className="flex items-center gap-2.5 p-2 rounded-lg
                          hover:bg-slate-800 transition-colors duration-150">
            <div className="w-8 h-8 bg-brand-600 rounded-full
                            flex items-center justify-center shrink-0">
              <span className="text-white text-xs font-bold">
                {getInitials(user?.username)}
              </span>
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-white text-xs font-semibold truncate leading-none">
                {user?.username}
              </p>
              <p className="text-slate-400 text-[11px] truncate mt-0.5">
                {user?.email}
              </p>
            </div>
            <button
              onClick={handleLogout}
              title="Logout"
              className="w-7 h-7 flex items-center justify-center rounded-md
                         text-slate-500 hover:text-red-400 hover:bg-slate-700
                         transition-all duration-150"
            >
              <FiLogOut size={14} />
            </button>
          </div>
        ) : (
          <button
            onClick={handleLogout}
            title="Logout"
            className="w-full flex justify-center p-2 rounded-lg
                       text-slate-400 hover:text-red-400
                       hover:bg-slate-800 transition-colors duration-150"
          >
            <FiLogOut size={15} />
          </button>
        )}
      </div>
    </aside>
  );
};

export default Sidebar;