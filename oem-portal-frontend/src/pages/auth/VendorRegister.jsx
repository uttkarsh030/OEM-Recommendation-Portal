import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../../api/axios';
import { FiArrowLeft, FiMail, FiPhone, FiBriefcase, FiKey } from 'react-icons/fi';

const VendorRegister = () => {
  const navigate = useNavigate();
  const [form, setForm]     = useState({ name: '', email: '', phone: '' });
  const [error, setError]   = useState('');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const res = await api.post('/auth/register/vendor', form);
      setResult(res.data.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  if (result) {
    const passwordMatch = result.message?.match(/password is: (.+)/);
    const generatedPassword = passwordMatch ? passwordMatch[1] : '—';

    return (
      <div className="min-h-screen bg-slate-50 flex items-center justify-center p-4">
        <div className="bg-white rounded-2xl shadow-card w-full max-w-md">
          <div className="p-8 text-center border-b border-slate-100">
            <div className="w-16 h-16 bg-brand-100 rounded-full 
                            flex items-center justify-center mx-auto mb-4">
              <FiBriefcase size={28} className="text-brand-600" />
            </div>
            <h2 className="text-xl font-bold text-slate-900">
              Welcome to OEM Portal!
            </h2>
            <p className="text-slate-500 text-sm mt-1">
              Your vendor account is ready
            </p>
          </div>

          <div className="p-8 space-y-4">
            <div className="bg-amber-50 border border-amber-200 rounded-xl p-4">
              <div className="flex items-center gap-2 mb-3">
                <FiKey className="text-amber-600" />
                <p className="text-sm font-semibold text-amber-800">
                  Save Your Credentials
                </p>
              </div>
              <div className="space-y-2">
                <div className="flex justify-between items-center">
                  <span className="text-xs text-amber-700">Email</span>
                  <span className="text-xs font-mono font-semibold text-amber-900 
                                   bg-amber-100 px-2 py-1 rounded">
                    {result.email}
                  </span>
                </div>
                <div className="flex justify-between items-center">
                  <span className="text-xs text-amber-700">Password</span>
                  <span className="text-xs font-mono font-semibold text-amber-900 
                                   bg-amber-100 px-2 py-1 rounded">
                    {generatedPassword}
                  </span>
                </div>
              </div>
              <p className="text-xs text-amber-600 mt-3 border-t border-amber-200 pt-3">
                ⚠️ This password will not be shown again. Please save it securely.
              </p>
            </div>

            <button
              onClick={() => navigate('/login')}
              className="btn-primary w-full py-2.5"
            >
              Continue to Login
            </button>
          </div>
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
            Vendor Registration
          </h1>
          <p className="text-slate-500 text-sm mt-1">
            Join the OEM recommendation portal
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
              { key: 'name',  label: 'Company Name',  icon: FiBriefcase, type: 'text',  placeholder: 'ABC Technologies' },
              { key: 'email', label: 'Email Address',  icon: FiMail,      type: 'email', placeholder: 'contact@company.com' },
              { key: 'phone', label: 'Phone Number',   icon: FiPhone,     type: 'tel',   placeholder: '9876543210' },
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

            <div className="bg-slate-50 rounded-lg p-3 border border-slate-200">
              <p className="text-xs text-slate-500">
                🔐 A secure password will be auto-generated and shown after registration.
              </p>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="btn-primary w-full py-2.5"
            >
              {loading ? 'Registering...' : 'Register as Vendor'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default VendorRegister;