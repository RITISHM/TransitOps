import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';

interface ProtectedRouteProps {
  allowedRoles?: string[];
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ allowedRoles }) => {
  const { token, role } = useAuthStore();

  if (!token) {
    return <Navigate to="/" replace />;
  }

  if (allowedRoles && role && !allowedRoles.includes(role)) {
    return (
      <div style={{ padding: '40px', textAlign: 'center' }}>
        <h2>Not Authorized</h2>
        <p>You do not have permission to access this page.</p>
        <button onClick={() => window.history.back()} className="btn btn-primary" style={{ width: 'auto' }}>Go Back</button>
      </div>
    );
  }

  return <Outlet />;
};

export default ProtectedRoute;
