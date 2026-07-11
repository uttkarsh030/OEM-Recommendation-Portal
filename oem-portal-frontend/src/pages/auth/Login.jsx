import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import api from '../../api/axios';
import { FiMail, FiLock, FiChevronDown } from 'react-icons/fi';
import { getRoleFromToken } from '../../utils/jwt';

const roles = [
  { value: 'ADMIN',           label: 'Administrator' },
  { value: 'BANKER',          label: 'Banker' },
  { value: 'VENDOR',          label: 'Vendor' },
  { value: 'DEPARTMENT_HEAD', label: 'Department Head' },
];

const routes = {
  ADMIN:           '/admin/dashboard',
  VENDOR:          '/vendor/dashboard',
  DEPARTMENT_HEAD: '/dh/dashboard',
  BANKER:          '/banker/dashboard',
};

const Login = () => {
  const navigate  = useNavigate();
  const { login } = useAuth();
  const [form, setForm]       = useState({ email: '', password: '', role: 'ADMIN' });
  const [error, setError]     = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const res  = await api.post('/auth/login', form);
      const data = res.data?.data;

      if (!data || !data.token) {
        setError('Server did not return a valid token');
        setLoading(false);
        return;
      }

      const roleFromToken = getRoleFromToken(data.token);

      if (!roleFromToken) {
        setError('Invalid token received');
        setLoading(false);
        return;
      }

      login(data);

      setTimeout(() => {
        const targetRoute = routes[roleFromToken] || '/login';
        navigate(targetRoute, { replace: true });
      }, 100);

    } catch (err) {
      setError(err.response?.data?.message || 'Invalid credentials');
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-900 flex">

      <div className="hidden lg:flex lg:w-1/2 bg-gradient-to-br 
                      from-brand-900 via-brand-800 to-slate-900
                      flex-col justify-between p-12">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 bg-brand-500 rounded-xl 
                          flex items-center justify-center">
            <span className="text-white font-bold text-lg">O</span>
          </div>
          <span className="text-white font-semibold text-lg">OEM Portal</span>
        </div>

        <div>
          <h1 className="text-4xl font-bold text-white leading-tight">
            OEM Recommendation
            <br />
            <span className="text-brand-400">Management System</span>
          </h1>
          <p className="text-slate-400 mt-4 text-base leading-relaxed">
            Streamline OEM recommendations, patches, and updates 
            across your banking organization with complete visibility 
            and audit trail.
          </p>
        </div>

        <p className="text-slate-600 text-xs">
          © 2024 OEM Portal. Banking Infrastructure.
        </p>
      </div>

      <div className="flex-1 flex items-center justify-center p-6 bg-white">
        <div className="w-full max-w-sm">

          <div className="mb-8">
            <h2 className="text-2xl font-bold text-slate-900">Welcome back</h2>
            <p className="text-slate-500 text-sm mt-1">
              Sign in to your account to continue
            </p>
          </div>

          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg 
                            px-4 py-3 text-sm text-red-700 mb-5 
                            flex items-center gap-2">
              <span>⚠️</span> {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-5">

            <div>
              <label className="label">Sign in as</label>
              <div className="relative">
                <select
                  className="input pr-10 appearance-none"
                  value={form.role}
                  onChange={(e) => setForm({ ...form, role: e.target.value })}
                >
                  {roles.map((r) => (
                    <option key={r.value} value={r.value}>{r.label}</option>
                  ))}
                </select>
                <FiChevronDown
                  size={16}
                  className="absolute right-3 top-1/2 -translate-y-1/2 
                             text-slate-400 pointer-events-none"
                />
              </div>
            </div>

            <div>
              <label className="label">Email address</label>
              <div className="relative">
                <FiMail size={16}
                        className="absolute left-3.5 top-1/2 -translate-y-1/2 
                                   text-slate-400" />
                <input
                  type="email"
                  className="input pl-10"
                  placeholder="you@example.com"
                  value={form.email}
                  onChange={(e) => setForm({ ...form, email: e.target.value })}
                  required
                />
              </div>
            </div>

            <div>
              <label className="label">Password</label>
              <div className="relative">
                <FiLock size={16}
                        className="absolute left-3.5 top-1/2 -translate-y-1/2 
                                   text-slate-400" />
                <input
                  type="password"
                  className="input pl-10"
                  placeholder="••••••••"
                  value={form.password}
                  onChange={(e) => setForm({ ...form, password: e.target.value })}
                  required
                />
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="btn-primary w-full py-2.5 text-base"
            >
              {loading ? 'Signing in...' : 'Sign in'}
            </button>
          </form>

          <div className="mt-8 space-y-3 pt-6 border-t border-slate-100">
            <p className="text-xs text-slate-400 text-center">
              Don't have an account?
            </p>
            <div className="grid grid-cols-2 gap-3">
              <Link to="/register/banker"
                    className="btn-secondary text-xs justify-center py-2.5">
                Register as Banker
              </Link>
              <Link to="/register/vendor"
                    className="btn-secondary text-xs justify-center py-2.5">
                Register as Vendor
              </Link>
            </div>
          </div>

        </div>
      </div>
    </div>
  );
};

export default Login;