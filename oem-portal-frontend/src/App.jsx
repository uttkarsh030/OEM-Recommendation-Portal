import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import { getRoleFromToken, isTokenExpired } from './utils/jwt';

import Login          from './pages/auth/Login';
import BankerRegister from './pages/auth/BankerRegister';
import VendorRegister from './pages/auth/VendorRegister';

import AdminDashboard  from './pages/admin/AdminDashboard';
import Departments     from './pages/admin/Departments';
import Bankers         from './pages/admin/Bankers';
import Vendors         from './pages/admin/Vendors';
import Recommendations from './pages/admin/Recommendations';
import AuditLogs       from './pages/admin/AuditLogs';

import VendorDashboard       from './pages/vendor/VendorDashboard';
import VendorRecommendations from './pages/vendor/VendorRecommendations';

import DHDashboard       from './pages/departmenthead/DHDashboard';
import DHRecommendations from './pages/departmenthead/DHRecommendations';
import DHBankers         from './pages/departmenthead/DHBankers';

import BankerDashboard       from './pages/banker/BankerDashboard';
import BankerRecommendations from './pages/banker/BankerRecommendations';

const roleRoutes = {
  ADMIN:           '/admin/dashboard',
  VENDOR:          '/vendor/dashboard',
  DEPARTMENT_HEAD: '/dh/dashboard',
  BANKER:          '/banker/dashboard',
};

const getValidRole = () => {
  const token = localStorage.getItem('token');
  
  if (!token || isTokenExpired(token)) {
    localStorage.removeItem('token');
    sessionStorage.removeItem('username');
    return null;
  }
  
  return getRoleFromToken(token);
};

const Guard = ({ children, role }) => {
  const { loading } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-slate-50">
        <div className="w-8 h-8 border-2 border-slate-200 border-t-brand-600 
                        rounded-full animate-spin" />
      </div>
    );
  }

  const userRole = getValidRole();

  if (!userRole) {
    return <Navigate to="/login" replace />;
  }

  if (role && userRole !== role) {
    return <Navigate to={roleRoutes[userRole] || '/login'} replace />;
  }

  return children;
};

const PublicRoute = ({ children }) => {
  const { loading } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-slate-50">
        <div className="w-8 h-8 border-2 border-slate-200 border-t-brand-600 
                        rounded-full animate-spin" />
      </div>
    );
  }

  const userRole = getValidRole();

  if (userRole) {
    return <Navigate to={roleRoutes[userRole] || '/'} replace />;
  }

  return children;
};

const Home = () => {
  const { loading } = useAuth();
  
  if (loading) return null;
  
  const userRole = getValidRole();
  
  if (!userRole) return <Navigate to="/login" replace />;
  
  return <Navigate to={roleRoutes[userRole] || '/login'} replace />;
};

const NotFound = () => (
  <div className="min-h-screen bg-slate-50 flex items-center justify-center">
    <div className="text-center">
      <p className="text-8xl font-bold text-slate-200">404</p>
      <h1 className="text-xl font-bold text-slate-900 mt-4">Page Not Found</h1>
      <a href="/" className="btn-primary mt-6 inline-flex">Go Home</a>
    </div>
  </div>
);

const AppRoutes = () => (
  <Routes>
    <Route path="/" element={<Home />} />

    <Route path="/login"           element={<PublicRoute><Login /></PublicRoute>} />
    <Route path="/register/banker" element={<PublicRoute><BankerRegister /></PublicRoute>} />
    <Route path="/register/vendor" element={<PublicRoute><VendorRegister /></PublicRoute>} />

    <Route path="/admin/dashboard"       element={<Guard role="ADMIN"><AdminDashboard /></Guard>} />
    <Route path="/admin/departments"     element={<Guard role="ADMIN"><Departments /></Guard>} />
    <Route path="/admin/bankers"         element={<Guard role="ADMIN"><Bankers /></Guard>} />
    <Route path="/admin/vendors"         element={<Guard role="ADMIN"><Vendors /></Guard>} />
    <Route path="/admin/recommendations" element={<Guard role="ADMIN"><Recommendations /></Guard>} />
    <Route path="/admin/audit-logs"      element={<Guard role="ADMIN"><AuditLogs /></Guard>} />

    <Route path="/vendor/dashboard"       element={<Guard role="VENDOR"><VendorDashboard /></Guard>} />
    <Route path="/vendor/recommendations" element={<Guard role="VENDOR"><VendorRecommendations /></Guard>} />

    <Route path="/dh/dashboard"       element={<Guard role="DEPARTMENT_HEAD"><DHDashboard /></Guard>} />
    <Route path="/dh/recommendations" element={<Guard role="DEPARTMENT_HEAD"><DHRecommendations /></Guard>} />
    <Route path="/dh/bankers"         element={<Guard role="DEPARTMENT_HEAD"><DHBankers /></Guard>} />

    <Route path="/banker/dashboard"       element={<Guard role="BANKER"><BankerDashboard /></Guard>} />
    <Route path="/banker/recommendations" element={<Guard role="BANKER"><BankerRecommendations /></Guard>} />

    <Route path="*" element={<NotFound />} />
  </Routes>
);

const App = () => (
  <BrowserRouter>
    <AuthProvider>
      <AppRoutes />
    </AuthProvider>
  </BrowserRouter>
);

export default App;