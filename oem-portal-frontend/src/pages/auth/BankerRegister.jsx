import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../../api/axios';
import { FiUser, FiMail, FiLock, FiArrowLeft, FiCheckCircle } from 'react-icons/fi';

const BankerRegister = () => {
  const navigate = useNavigate();
  const [form, setForm]       = useState({ username: '', email: '', password: '' });
  const [error, setError]     = useState('');
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await api.post('/auth/register/banker', form);
      setSuccess(true);
      setTimeout(() => navigate('/login'), 4000);
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  if (success) {
    return (
      <div className="min-h-screen bg-slate-50 flex items-center justify-center p-4">
        <div className="bg-white rounded-2xl shadow-card p-10 
                        max-w-md w-full text-center">
          <div className="w-16 h-16 bg-emerald-100 rounded-full 
                          flex items-center justify-center mx-auto mb-5">
            <FiCheckCircle size={32} className="text-emerald-600" />
          </div>
          <h2 className="text-xl font-bold text-slate-900">
            Registration Successful!
          </h2>
          <p className="text-slate-500 text-sm mt-2 leading-relaxed">
            Your account has been created and is pending admin approval.
            You will be notified once approved.
          </p>
          <div className="bg-amber-50 border border-amber-200 rounded-lg 
                          p-4 mt-6 text-left">
            <p className="text-amber-800 text-sm font-medium">
              ⏳ What happens next?
            </p>
            <ul className="text-amber-700 text-xs mt-2 space-y-1">
              <li>• Admin reviews your registration</li>
              <li>• Account gets approved and activated</li>
              <li>• You get assigned to a department</li>
              <li>• You can then login and start working</li>
            </ul>
          </div>
          <p className="text-xs text-slate-400 mt-6">
            Redirecting to login in a moment...
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50 flex items-center justify-center p-4">
      <div className="bg-white rounded-2xl shadow-card w-full max-w-md">

        <div className="p-8 border-b border-slate-100">
          <Link to="/login"
                className="inline-flex items-center gap-1.5 text-slate-500 
                           hover:text-slate-700 text-sm mb-6 transition-colors">
            <FiArrowLeft size={15} /> Back to login
          </Link>
          <h1 className="text-2xl font-bold text-slate-900">
            Banker Registration
          </h1>
          <p className="text-slate-500 text-sm mt-1">
            Create your account to get started
          </p>
        </div>

        <div className="p-8">
          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg 
                            px-4 py-3 text-sm text-red-700 mb-5">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-5">
            {[
              { key: 'username', label: 'Username',  icon: FiUser, type: 'text',     placeholder: 'johndoe' },
              { key: 'email',    label: 'Email',     icon: FiMail, type: 'email',    placeholder: 'john@bank.com' },
              { key: 'password', label: 'Password',  icon: FiLock, type: 'password', placeholder: '••••••••' },
            ].map(({ key, label, icon: Icon, type, placeholder }) => (
              <div key={key}>
                <label className="label">{label}</label>
                <div className="relative">
                  <Icon size={16}
                        className="absolute left-3.5 top-1/2 
                                   -translate-y-1/2 text-slate-400" />
                  <input
                    type={type}
                    className="input pl-10"
                    placeholder={placeholder}
                    value={form[key]}
                    onChange={(e) => setForm({ ...form, [key]: e.target.value })}
                    required
                  />
                </div>
              </div>
            ))}

            <button
              type="submit"
              disabled={loading}
              className="btn-primary w-full py-2.5"
            >
              {loading ? 'Creating account...' : 'Create Account'}
            </button>
          </form>
        </div>

      </div>
    </div>
  );
};

export default BankerRegister;