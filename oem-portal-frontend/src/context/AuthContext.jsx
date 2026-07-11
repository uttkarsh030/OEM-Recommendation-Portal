import { createContext, useContext, useState, useEffect, useCallback } from 'react';
import {
  getRoleFromToken,
  getEmailFromToken,
  isTokenExpired,
} from '../utils/jwt';

const AuthContext = createContext(null);

const buildUserFromToken = (token, username) => {
  if (!token || isTokenExpired(token)) return null;

  const role  = getRoleFromToken(token);
  const email = getEmailFromToken(token);

  if (!role) return null;

  return { token, role, email, username };
};

export const AuthProvider = ({ children }) => {
  const [user,    setUser]    = useState(null);
  const [loading, setLoading] = useState(true);

  // Restore session on mount
  useEffect(() => {
    const token    = localStorage.getItem('token');
    const username = sessionStorage.getItem('username');
    const userData = buildUserFromToken(token, username);

    if (userData) {
      setUser(userData);
    } else if (token) {
      // Token present but invalid/expired — clean up
      localStorage.removeItem('token');
      sessionStorage.removeItem('username');
    }

    setLoading(false);
  }, []);

  const login = useCallback((data) => {
    localStorage.setItem('token',    data.token);
    sessionStorage.setItem('username', data.username);
    setUser(buildUserFromToken(data.token, data.username));
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    sessionStorage.removeItem('username');
    setUser(null);
  }, []);

  return (
    <AuthContext.Provider value={{ user, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used inside <AuthProvider>');
  return ctx;
};