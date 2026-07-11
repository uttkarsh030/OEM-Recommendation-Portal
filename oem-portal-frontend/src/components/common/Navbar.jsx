import { useAuth } from '../../context/AuthContext';
import { FiBell } from 'react-icons/fi';
import { getInitials } from '../../utils/helpers';

const roleColors = {
  ADMIN:           'bg-red-500',
  VENDOR:          'bg-purple-500',
  DEPARTMENT_HEAD: 'bg-brand-500',
  BANKER:          'bg-emerald-500',
};

const roleLabel = {
  ADMIN:           'Administrator',
  VENDOR:          'Vendor',
  DEPARTMENT_HEAD: 'Department Head',
  BANKER:          'Banker',
};

const Navbar = ({ title, subtitle }) => {
  const { user } = useAuth();

  return (
    <header className="h-16 bg-white border-b border-slate-100
                       flex items-center justify-between px-6
                       sticky top-0 z-20">

      {/* Page title */}
      <div>
        {title && (
          <h1 className="text-base font-semibold text-slate-900 leading-none tracking-tight">
            {title}
          </h1>
        )}
        {subtitle && (
          <p className="text-xs text-slate-400 mt-0.5 font-medium">
            {subtitle}
          </p>
        )}
      </div>

      {/* Right side */}
      <div className="flex items-center gap-1.5">

        {/* Notification bell */}
        <button className="btn-ghost relative">
          <FiBell size={17} />
          <span className="absolute top-2 right-2 w-1.5 h-1.5
                           bg-red-500 rounded-full" />
        </button>

        {/* Divider */}
        <div className="w-px h-6 bg-slate-100 mx-1.5" />

        {/* User info */}
        <div className="flex items-center gap-2.5">
          <div className={`
            w-8 h-8 rounded-full flex items-center justify-center shrink-0
            ${roleColors[user?.role] || 'bg-slate-400'}
          `}>
            <span className="text-white text-xs font-bold">
              {getInitials(user?.username)}
            </span>
          </div>
          <div className="hidden sm:block">
            <p className="text-sm font-semibold text-slate-900 leading-none">
              {user?.username}
            </p>
            <p className="text-[11px] text-slate-400 mt-0.5 font-medium">
              {roleLabel[user?.role]}
            </p>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Navbar;