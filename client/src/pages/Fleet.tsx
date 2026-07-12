import React, { useState, useEffect } from 'react';

const MOCK_VEHICLES = [
  { id: '1', regNo: 'GJ01AB452', name: 'VAN-05', type: 'Van', capacity: '500 kg', odometer: '74,000', cost: '6,20,000', status: 'Available' },
  { id: '2', regNo: 'GJ01AB998', name: 'TRUCK-11', type: 'Truck', capacity: '5 Ton', odometer: '182,000', cost: '24,50,000', status: 'Available' },
  { id: '3', regNo: 'GJ01AB1120', name: 'MINI-03', type: 'Mini', capacity: '1 Ton', odometer: '66,000', cost: '4,10,000', status: 'In Shop' },
  { id: '4', regNo: 'GJ01AB008', name: 'VAN-09', type: 'Van', capacity: '750 kg', odometer: '241,900', cost: '5,90,000', status: 'Retired' },
];

const Fleet = () => {
  const [vehicles, setVehicles] = useState(MOCK_VEHICLES);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setTimeout(() => {
      setIsLoading(false);
    }, 400);
  }, []);

  const getStatusBadge = (status: string) => {
    switch (status) {
      case 'Available': return 'badge-green';
      case 'In Shop': return 'badge-orange';
      case 'Retired': return 'badge-red';
      default: return 'badge-gray';
    }
  };

  if (isLoading) return <div className="loading-state">Loading fleet data...</div>;

  return (
    <div className="fleet-container" style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      
      {/* Header & Filters */}
      <div className="page-header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div className="filters-row" style={{ display: 'flex', gap: '16px' }}>
          <select className="filter-select">
            <option>Type: All</option>
            <option>Van</option>
            <option>Truck</option>
            <option>Mini</option>
          </select>
          <select className="filter-select">
            <option>Status: All</option>
            <option>Available</option>
            <option>In Shop</option>
            <option>Retired</option>
          </select>
          <div className="search-bar" style={{ width: '250px' }}>
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" style={{ marginRight: '8px', color: '#9ca3af' }}><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
            <input type="text" placeholder="Search reg. no..." className="search-input" />
          </div>
        </div>
        <button className="btn btn-primary" style={{ width: 'auto', padding: '10px 20px', marginTop: 0 }}>
          + Add Vehicle
        </button>
      </div>

      {/* Table Panel */}
      <div className="panel" style={{ padding: '0', overflow: 'hidden' }}>
        <div className="table-responsive">
          <table className="clean-table" style={{ margin: '0' }}>
            <thead>
              <tr>
                <th style={{ padding: '24px' }}>REG. NO. (UNIQUE)</th>
                <th>NAME/MODEL</th>
                <th>TYPE</th>
                <th>CAPACITY</th>
                <th>ODOMETER</th>
                <th>ACQ. COST</th>
                <th>STATUS</th>
              </tr>
            </thead>
            <tbody>
              {vehicles.map(v => (
                <tr key={v.id}>
                  <td style={{ padding: '16px 24px', fontWeight: 600 }}>{v.regNo}</td>
                  <td>{v.name}</td>
                  <td>{v.type}</td>
                  <td>{v.capacity}</td>
                  <td>{v.odometer}</td>
                  <td>{v.cost}</td>
                  <td>
                    <span className={`status-badge ${getStatusBadge(v.status)}`}>{v.status}</span>
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

export default Fleet;
