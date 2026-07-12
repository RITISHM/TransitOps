import React, { useState } from 'react';
import { Link } from 'react-router-dom';

const Register = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('');
  const [errors, setErrors] = useState<Record<string, string>>({});

  const validate = () => {
    const newErrors: Record<string, string> = {};
    if (name.trim().length < 2) newErrors.name = 'Name must be at least 2 characters long';
    if (!email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) newErrors.email = 'Please enter a valid email address';
    if (password.length < 6) newErrors.password = 'Password must be at least 6 characters long';
    if (!role) newErrors.role = 'Please select a role';
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (validate()) {
      console.log('Registration attempt:', { name, email, password, role });
      alert('Registration successful!'); // Mock success
    }
  };

  return (
    <div className="auth-layout">
      
      {/* LEFT SIDE: FORM */}
      <div className="auth-left">
        <div className="auth-form-wrapper">
          <h1 className="auth-title">Create an Account</h1>
          <p className="auth-subtitle">
            Join TransitOps to simplify your transport and logistics workflows.
          </p>

          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <input
                type="text"
                id="name"
                className={`form-control ${errors.name ? 'input-error' : ''}`}
                placeholder="Full Name"
                value={name}
                onChange={(e) => {
                  setName(e.target.value);
                  if (errors.name) setErrors({...errors, name: ''});
                }}
                required
                style={{ borderColor: errors.name ? '#ef4444' : undefined }}
              />
              {errors.name && <span style={{ color: '#ef4444', fontSize: '0.75rem', marginTop: '4px', display: 'block' }}>{errors.name}</span>}
            </div>

            <div className="form-group">
              <input
                type="email"
                id="email"
                className={`form-control ${errors.email ? 'input-error' : ''}`}
                placeholder="Email address"
                value={email}
                onChange={(e) => {
                  setEmail(e.target.value);
                  if (errors.email) setErrors({...errors, email: ''});
                }}
                required
                style={{ borderColor: errors.email ? '#ef4444' : undefined }}
              />
              {errors.email && <span style={{ color: '#ef4444', fontSize: '0.75rem', marginTop: '4px', display: 'block' }}>{errors.email}</span>}
            </div>

            <div className="form-group">
              <input
                type="password"
                id="password"
                className={`form-control ${errors.password ? 'input-error' : ''}`}
                placeholder="Password"
                value={password}
                onChange={(e) => {
                  setPassword(e.target.value);
                  if (errors.password) setErrors({...errors, password: ''});
                }}
                required
                style={{ borderColor: errors.password ? '#ef4444' : undefined }}
              />
              {errors.password && <span style={{ color: '#ef4444', fontSize: '0.75rem', marginTop: '4px', display: 'block' }}>{errors.password}</span>}
            </div>

            <div className="form-group">
              <select
                id="role"
                className={`form-control ${errors.role ? 'input-error' : ''}`}
                value={role}
                onChange={(e) => {
                  setRole(e.target.value);
                  if (errors.role) setErrors({...errors, role: ''});
                }}
                required
                style={{ appearance: 'auto', WebkitAppearance: 'none', borderColor: errors.role ? '#ef4444' : undefined }}
              >
                <option value="" disabled>Select Role (RBAC)</option>
                <option value="Fleet Manager">Fleet Manager</option>
                <option value="Dispatcher">Dispatcher</option>
                <option value="Safety Officer">Safety Officer</option>
                <option value="Financial Analyst">Financial Analyst</option>
              </select>
              {errors.role && <span style={{ color: '#ef4444', fontSize: '0.75rem', marginTop: '4px', display: 'block' }}>{errors.role}</span>}
            </div>

            <button type="submit" className="btn btn-primary" style={{ marginTop: '24px' }}>
              Create Account
            </button>
          </form>

          <div className="divider">or continue with</div>

          <div className="social-buttons">
            <button className="social-btn">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
                <polyline points="22,6 12,13 2,6"></polyline>
              </svg>
            </button>
            <button className="social-btn">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M12 20.94c1.5 0 2.75 1.06 4 1.06 3 0 6-8 6-12.22A4.91 4.91 0 0 0 17 5c-2.22 0-4 1.44-5 2-1-.56-2.78-2-5-2a4.9 4.9 0 0 0-5 4.78C2 14 5 22 8 22c1.25 0 2.5-1.06 4-1.06Z"></path>
                <path d="M10 2c1 .5 2 2 2 5h-2c0-3-1-4-2-5Z"></path>
              </svg>
            </button>
            <button className="social-btn">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z"></path>
              </svg>
            </button>
          </div>

          <p className="register-text">
            Already have an account? <Link to="/" className="link">Login here</Link>
          </p>
        </div>
      </div>

      {/* RIGHT SIDE: ILLUSTRATION PANEL */}
      <div className="auth-right">
        <img 
          src="/register-illustration.png" 
          alt="Illustration" 
          className="illustration-img"
        />
        
        <div className="carousel-dots">
          <div className="dot"></div>
          <div className="dot active"></div>
          <div className="dot"></div>
        </div>

        <h2 className="panel-text">
          Make your transport operations easier and organized with TransitOps
        </h2>
      </div>

    </div>
  );
};

export default Register;
