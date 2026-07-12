import React, { useState, useEffect } from 'react';
import api from '../services/api';

// --- MOCK DATA ---
const MOCK_TRIPS = [
  { id: 'TROO1', vehicle: 'VAN-05', driver: 'Alex', status: 'On Trip', eta: '45 min', price: '$240.00', change: '+13.38%' },
  { id: 'TROO2', vehicle: 'TRK-12', driver: 'John', status: 'Completed', eta: '-', price: '$750.50', change: '+11.19%' },
  { id: 'TROO3', vehicle: 'MINI-09', driver: 'Priya', status: 'Dispatched', eta: '1h 10m', price: '$120.00', change: '+7.57%' },
  { id: 'TROO4', vehicle: '-', driver: '-', status: 'Draft', eta: 'Awaiting', price: '$0.00', change: '0.00%' },
];

const Dashboard = () => {
  const [trips, setTrips] = useState(MOCK_TRIPS);
  const [kpis, setKpis] = useState<any>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchKpis = async () => {
      try {
        const response = await api.get('/dashboard');
        setKpis(response.data);
      } catch (err) {
        console.error("Failed to fetch KPIs", err);
      } finally {
        setIsLoading(false);
      }
    };
    fetchKpis();
  }, []);

  if (isLoading) return <div className="loading-state">Loading overview...</div>;

  return (
    <div className="dashboard-container">
      
      {/* TOP SECTION: METRICS */}
      <div className="metrics-section">
        <div className="metrics-col-left">
          <h3 className="section-subtitle">Active Fleet</h3>
          <div className="hero-card">
            <div className="hero-card-header">
              <div className="hero-value">{kpis?.activeVehicles || 0} Vehicles</div>
              <div className="hero-badge" style={{ backgroundColor: 'var(--color-accent-active)' }}>
                {kpis?.fleetUtilizationPercentage || 0}% Utilized
              </div>
            </div>
            <p className="hero-subtext">Total active fleet balance</p>
            
            {/* Mock Chart Line */}
            <div className="mock-chart">
              <svg viewBox="0 0 400 100" preserveAspectRatio="none">
                <path d="M0,80 Q20,70 40,80 T80,60 T120,70 T160,40 T200,60 T240,30 T280,40 T320,10 T360,20 T400,0 L400,100 L0,100 Z" fill="rgba(59, 130, 246, 0.1)" />
                <path d="M0,80 Q20,70 40,80 T80,60 T120,70 T160,40 T200,60 T240,30 T280,40 T320,10 T360,20 T400,0" fill="none" stroke="#3b82f6" strokeWidth="2" />
                <circle cx="320" cy="10" r="4" fill="#3b82f6" />
              </svg>
            </div>
            <div className="chart-times">
              <span>1H</span><span>24H</span><span className="active">1W</span><span>1M</span><span>1Y</span><span>ALL</span>
            </div>
          </div>
        </div>

        <div className="metrics-col-right">
          <h3 className="section-subtitle flex-between">
            Key Metrics
            <button className="icon-btn-small">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><line x1="4" y1="21" x2="4" y2="14"></line><line x1="4" y1="10" x2="4" y2="3"></line><line x1="12" y1="21" x2="12" y2="12"></line><line x1="12" y1="8" x2="12" y2="3"></line><line x1="20" y1="21" x2="20" y2="16"></line><line x1="20" y1="12" x2="20" y2="3"></line><line x1="1" y1="14" x2="7" y2="14"></line><line x1="9" y1="8" x2="15" y2="8"></line><line x1="17" y1="16" x2="23" y2="16"></line></svg>
            </button>
          </h3>
          <div className="small-cards-grid">
            <div className="metric-card bg-purple">
              <div className="card-top">
                <h4>{kpis?.activeTrips || 0} Trips</h4>
                <button className="more-btn">⋮</button>
              </div>
              <p>Active</p>
              <div className="card-bottom">
                <span className="icon-box">T</span>
                <span className="change positive">+1.14%</span>
              </div>
            </div>
            
            <div className="metric-card bg-green">
              <div className="card-top">
                <h4>{kpis?.driversOnDuty || 0} Drivers</h4>
                <button className="more-btn">⋮</button>
              </div>
              <p>On Duty</p>
              <div className="card-bottom">
                <span className="icon-box">D</span>
                <span className="change positive">+0.31%</span>
              </div>
            </div>

            <div className="metric-card bg-yellow">
              <div className="card-top">
                <h4 style={{ color: (kpis?.vehiclesInMaintenance || 0) > 0 ? 'var(--color-accent-alert)' : 'inherit' }}>
                  {kpis?.vehiclesInMaintenance || 0} Issues
                </h4>
                <button className="more-btn">⋮</button>
              </div>
              <p>Maintenance</p>
              <div className="card-bottom">
                <span className="icon-box">M</span>
                <span className="change negative">-0.27%</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* BOTTOM SECTION */}
      <div className="bottom-section">
        <div className="list-panel">
          <div className="list-header flex-between">
            <h2 className="list-title">Recent Trips <span className="text-secondary" style={{fontSize: '1rem', fontWeight: 500}}> down 0.80%</span></h2>
            <div className="list-filters">
              <select className="pill-select"><option>24h</option></select>
              <select className="pill-select"><option>Top active</option></select>
            </div>
          </div>

          <table className="clean-table">
            <thead>
              <tr>
                <th>Name / Vehicle</th>
                <th>Revenue</th>
                <th>Change</th>
                <th>Status</th>
                <th>Watch</th>
              </tr>
            </thead>
            <tbody>
              {trips.map(trip => (
                <tr key={trip.id}>
                  <td>
                    <div className="row-title">
                      <div className="row-icon">{(trip.vehicle.charAt(0) || '-')}</div>
                      <div>
                        <div className="fw-bold">{trip.driver}</div>
                        <div className="text-secondary text-sm">{trip.vehicle}</div>
                      </div>
                    </div>
                  </td>
                  <td className="fw-bold">{trip.price}</td>
                  <td className={`fw-bold ${trip.change.startsWith('+') ? 'text-green' : 'text-gray'}`}>{trip.change}</td>
                  <td className="fw-bold">{trip.status}</td>
                  <td>
                    <button className="star-btn">
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"></polygon></svg>
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

    </div>
  );
};

export default Dashboard;
