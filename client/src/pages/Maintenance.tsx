import React, { useState } from 'react';

const MOCK_SERVICE_LOG = [
  { id: '1', vehicle: 'VAN-05', service: 'Oil Change', cost: '2,500', status: 'In Shop' },
  { id: '2', vehicle: 'TRUCK-11', service: 'Engine Repair', cost: '18,000', status: 'Completed' },
  { id: '3', vehicle: 'MINI-03', service: 'Tyre Replace', cost: '6,200', status: 'In Shop' },
];

const Maintenance = () => {
  const [vehicle, setVehicle] = useState('VAN-05');
  const [serviceType, setServiceType] = useState('Oil Change');
  const [cost, setCost] = useState('2500');
  const [date, setDate] = useState('07/07/2026');
  const [status, setStatus] = useState('Active');
  const [logs, setLogs] = useState(MOCK_SERVICE_LOG);

  const getStatusBadge = (status: string) => {
    switch (status) {
      case 'Completed': return 'badge-green';
      case 'In Shop': return 'badge-orange';
      default: return 'badge-gray';
    }
  };

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    // In a real app this would post to an API.
    alert(`Service record saved for ${vehicle}`);
  };

  return (
    <div className="maintenance-container" style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '32px' }}>
      
      {/* LEFT COLUMN: LOG SERVICE RECORD */}
      <div className="left-col">
        <div className="panel" style={{ padding: '32px' }}>
          <h3 style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', textTransform: 'uppercase', marginBottom: '24px' }}>LOG SERVICE RECORD</h3>
          
          <form onSubmit={handleSave}>
            <div className="form-group">
              <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Vehicle</label>
              <input type="text" className="form-control" value={vehicle} onChange={e => setVehicle(e.target.value)} style={{ padding: '12px 20px' }} />
            </div>

            <div className="form-group">
              <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Service Type</label>
              <input type="text" className="form-control" value={serviceType} onChange={e => setServiceType(e.target.value)} style={{ padding: '12px 20px' }} />
            </div>

            <div className="form-group">
              <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Cost</label>
              <input type="number" className="form-control" value={cost} onChange={e => setCost(e.target.value)} style={{ padding: '12px 20px' }} />
            </div>

            <div className="form-group">
              <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Date</label>
              <input type="text" className="form-control" value={date} onChange={e => setDate(e.target.value)} style={{ padding: '12px 20px' }} />
            </div>

            <div className="form-group" style={{ marginBottom: '32px' }}>
              <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Status</label>
              <input type="text" className="form-control" value={status} onChange={e => setStatus(e.target.value)} style={{ padding: '12px 20px' }} />
            </div>

            <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: 0, backgroundColor: '#d97706', borderColor: '#d97706' }}>
              Save
            </button>
          </form>
        </div>
      </div>

      {/* RIGHT COLUMN: SERVICE LOG */}
      <div className="right-col">
        <h3 style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', textTransform: 'uppercase', marginBottom: '24px' }}>SERVICE LOG</h3>
        
        <div className="panel" style={{ padding: '0', overflow: 'hidden' }}>
          <div className="table-responsive">
            <table className="clean-table" style={{ margin: '0' }}>
              <thead>
                <tr>
                  <th style={{ padding: '24px' }}>VEHICLE</th>
                  <th>SERVICE</th>
                  <th>COST</th>
                  <th>STATUS</th>
                </tr>
              </thead>
              <tbody>
                {logs.map(log => (
                  <tr key={log.id}>
                    <td style={{ padding: '16px 24px', fontWeight: 600 }}>{log.vehicle}</td>
                    <td>{log.service}</td>
                    <td>{log.cost}</td>
                    <td>
                      <span className={`status-badge ${getStatusBadge(log.status)}`}>{log.status}</span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

    </div>
  );
};

export default Maintenance;
