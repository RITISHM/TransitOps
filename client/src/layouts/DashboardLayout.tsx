import { Outlet, Link, useLocation } from 'react-router-dom';
import { useState } from 'react';

const Sidebar = () => {
  const location = useLocation();
  
  const navItems = [
    { name: 'Dashboard', path: '/dashboard' },
    { name: 'Fleet', path: '/fleet' },
    { name: 'Drivers', path: '/drivers' },
    { name: 'Trips', path: '/trips' },
    { name: 'Maintenance', path: '/maintenance' },
    { name: 'Fuel & Expenses', path: '/fuel' },
    { name: 'Analytics', path: '/analytics' },
    { name: 'Settings', path: '/settings' },
  ];

  return (
    <aside className="sidebar">
      <div className="sidebar-logo">
        TransitOps
      </div>
      <nav className="sidebar-nav">
        {navItems.map((item) => (
          <Link
            key={item.name}
            to={item.path}
            className={`nav-item ${location.pathname === item.path ? 'active' : ''}`}
          >
            {item.name}
          </Link>
        ))}
      </nav>
    </aside>
  );
};

<<<<<<< HEAD
const Header = () => {
  const [isProfileOpen, setIsProfileOpen] = useState(false);
=======
import { useThemeStore } from '../store/themeStore';
import { useAuthStore } from '../store/authStore';

const Header = () => {
  const [isProfileOpen, setIsProfileOpen] = useState(false);
  const { theme, toggleTheme } = useThemeStore();
  const { user, logout } = useAuthStore();

  const handleLogout = () => {
    logout();
    setIsProfileOpen(false);
  };
>>>>>>> c04aa2e22828387e4ac65ad6ee4adfbfd54d01d5

  return (
    <header className="top-header">
      <h1 className="header-title">Overview</h1>
      
      <div className="header-actions">
<<<<<<< HEAD
        <button className="icon-btn">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
=======
        <button className="icon-btn" onClick={toggleTheme} title="Toggle Theme">
          {theme === 'light' ? (
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path></svg>
          ) : (
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><circle cx="12" cy="12" r="5"></circle><line x1="12" y1="1" x2="12" y2="3"></line><line x1="12" y1="21" x2="12" y2="23"></line><line x1="4.22" y1="4.22" x2="5.64" y2="5.64"></line><line x1="18.36" y1="18.36" x2="19.78" y2="19.78"></line><line x1="1" y1="12" x2="3" y2="12"></line><line x1="21" y1="12" x2="23" y2="12"></line><line x1="4.22" y1="19.78" x2="5.64" y2="18.36"></line><line x1="18.36" y1="5.64" x2="19.78" y2="4.22"></line></svg>
          )}
>>>>>>> c04aa2e22828387e4ac65ad6ee4adfbfd54d01d5
        </button>
        <button className="icon-btn">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path><path d="M13.73 21a2 2 0 0 1-3.46 0"></path></svg>
        </button>
        
        <div style={{ position: 'relative' }}>
          <div className="user-profile-pill" onClick={() => setIsProfileOpen(!isProfileOpen)} style={{ cursor: 'pointer' }}>
<<<<<<< HEAD
            <div className="avatar-img">RK</div>
            <span className="user-name">Raven K.</span>
=======
            <div className="avatar-img">{user?.email?.charAt(0).toUpperCase() || 'U'}</div>
            <span className="user-name">{user?.email || 'User'}</span>
>>>>>>> c04aa2e22828387e4ac65ad6ee4adfbfd54d01d5
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><polyline points="6 9 12 15 18 9"></polyline></svg>
          </div>
          
          {isProfileOpen && (
            <div className="panel" style={{ 
              position: 'absolute', top: '100%', right: 0, marginTop: '8px', padding: '8px 0', 
              minWidth: '160px', zIndex: 100, display: 'flex', flexDirection: 'column' 
            }}>
              <Link to="/settings" onClick={() => setIsProfileOpen(false)} style={{ padding: '10px 16px', color: 'var(--text-primary)', textDecoration: 'none', fontSize: '0.85rem', fontWeight: 500 }}>Settings</Link>
              <div style={{ height: '1px', backgroundColor: 'var(--border-color)', margin: '4px 0' }}></div>
<<<<<<< HEAD
              <Link to="/" onClick={() => setIsProfileOpen(false)} style={{ padding: '10px 16px', color: '#ef4444', textDecoration: 'none', fontSize: '0.85rem', fontWeight: 500 }}>Logout</Link>
=======
              <Link to="/" onClick={handleLogout} style={{ padding: '10px 16px', color: '#ef4444', textDecoration: 'none', fontSize: '0.85rem', fontWeight: 500 }}>Logout</Link>
>>>>>>> c04aa2e22828387e4ac65ad6ee4adfbfd54d01d5
            </div>
          )}
        </div>
      </div>
    </header>
  );
};

const DashboardLayout = () => {
  return (
    <div className="app-layout">
      <Sidebar />
      <div className="main-content">
        <Header />
        <main className="page-content">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default DashboardLayout;
