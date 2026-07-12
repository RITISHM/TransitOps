import React, { useState, useEffect } from 'react';

const MOCK_DRIVERS = [
  { id: '1', name: 'Alex', license: 'DL-88213', category: 'LMV', expiry: '12/2028', contact: '98765xxxxx', tripCompl: '96%', safety: 'Available', status: 'Available' },
  { id: '2', name: 'John', license: 'DL-44120', category: 'HMV', expiry: '03/2025 EXPIRING', contact: '98220xxxxx', tripCompl: '81%', safety: 'Suspended', status: 'Suspended' },
  { id: '3', name: 'Priya', license: 'DL-77031', category: 'LMV', expiry: '08/2029', contact: '99110xxxxx', tripCompl: '99%', safety: 'On Trip', status: 'On Trip' },
  { id: '4', name: 'Suresh', license: 'DL-90045', category: 'HMV', expiry: '01/2027', contact: '97440xxxxx', tripCompl: '88%', safety: 'Available', status: 'Off Duty' },
];

const Drivers = () => {
  const [drivers, setDrivers] = useState(MOCK_DRIVERS);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setTimeout(() => {
      setIsLoading(false);
    }, 400);
  }, []);

  const getBadgeClass = (status: string) => {
    switch (status) {
      case 'Available': return 'badge-green';
      case 'On Trip': return 'badge-blue';
      case 'Suspended': return 'badge-orange';
      case 'Off Duty': return 'badge-gray';
      default: return 'badge-gray';
    }
  };

  if (isLoading) return <div className="loading-state">Loading drivers...</div>;

  return (
    <div className="drivers-container" style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      
      {/* Header & Filters */}
      <div className="page-header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div className="search-bar" style={{ width: '300px' }}>
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" style={{ marginRight: '8px', color: '#9ca3af' }}><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
          <input type="text" placeholder="Search..." className="search-input" />
        </div>
        <button className="btn btn-primary" style={{ width: 'auto', padding: '10px 20px', marginTop: 0 }}>
          + Add Driver
        </button>
      </div>

      {/* Table Panel */}
      <div className="panel" style={{ padding: '0', overflow: 'hidden' }}>
        <div className="table-responsive">
          <table className="clean-table" style={{ margin: '0' }}>
            <thead>
              <tr>
                <th style={{ padding: '24px' }}>DRIVER</th>
                <th>LICENSE NO</th>
                <th>CATEGORY</th>
                <th>EXPIRY</th>
                <th>CONTACT</th>
                <th>TRIP COMPL.</th>
                <th>SAFETY</th>
                <th>STATUS</th>
              </tr>
            </thead>
            <tbody>
              {drivers.map(d => (
                <tr key={d.id}>
                  <td style={{ padding: '16px 24px', fontWeight: 600 }}>{d.name}</td>
                  <td>{d.license}</td>
                  <td>{d.category}</td>
                  <td style={{ color: d.expiry.includes('EXPIRING') ? '#ef4444' : 'inherit' }}>{d.expiry}</td>
                  <td>{d.contact}</td>
                  <td style={{ fontWeight: 600 }}>{d.tripCompl}</td>
                  <td>
                    <span className={`status-badge ${getBadgeClass(d.safety)}`}>{d.safety}</span>
                  </td>
                  <td>
                    <span className={`status-badge ${getBadgeClass(d.status)}`}>{d.status}</span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Toggle Stat Filters */}
      <div style={{ padding: '8px' }}>
        <h3 style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', textTransform: 'uppercase', marginBottom: '16px' }}>TOGGLE STAT</h3>
        <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
          <button className="status-badge badge-green" style={{ border: 'none', cursor: 'pointer' }}>Available</button>
          <button className="status-badge badge-blue" style={{ border: 'none', cursor: 'pointer' }}>On Trip</button>
          <button className="status-badge badge-gray" style={{ border: 'none', cursor: 'pointer' }}>Off Duty</button>
          <button className="status-badge badge-orange" style={{ border: 'none', cursor: 'pointer' }}>Suspended</button>
        </div>
      </div>

    </div>
  );
};

export default Drivers;
