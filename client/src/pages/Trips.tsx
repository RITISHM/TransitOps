import React, { useState } from 'react';

const MOCK_LIVE_BOARD = [
  { id: 'TR001', vehicle: 'VAN-05', driver: 'ALEX', source: 'Gandhinagar Depot', dest: 'Ahmedabad Hub', status: 'Dispatched', meta: '45 min' },
  { id: 'TR004', vehicle: 'TRUCK-04', driver: 'SURESH', source: 'Vatva Industrial Area', dest: 'Sanand Warehouse', status: 'Draft', meta: 'Awaiting driver' },
  { id: 'TR006', vehicle: 'Unassigned', driver: '', source: 'Mansa', dest: 'Kalol Depot', status: 'Cancelled', meta: 'Vehicle went to shop' },
];

const Trips = () => {
  const [source, setSource] = useState('Gandhinagar Depot');
  const [destination, setDestination] = useState('Ahmedabad Hub');
  const [vehicle, setVehicle] = useState('VAN-05 - 500 kg capacity');
  const [driver, setDriver] = useState('Alex');
  const [weight, setWeight] = useState('700');
  const [distance, setDistance] = useState('38');

  // Validation logic
  const capacity = 500;
  const weightNum = parseInt(weight) || 0;
  const isOverweight = weightNum > capacity;

  const getStatusBadge = (status: string) => {
    switch (status) {
      case 'Dispatched': return 'badge-blue';
      case 'Draft': return 'badge-gray';
      case 'Cancelled': return 'badge-red';
      case 'Completed': return 'badge-green';
      default: return 'badge-gray';
    }
  };

  return (
    <div className="trips-container" style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '32px' }}>
      
      {/* LEFT COLUMN: CREATE TRIP & LIFECYCLE */}
      <div className="left-col" style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
        
        {/* Trip Lifecycle Stepper */}
        <div className="panel" style={{ padding: '24px' }}>
          <h3 style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', textTransform: 'uppercase', marginBottom: '0' }}>TRIP LIFECYCLE</h3>
          <div className="stepper" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '24px', position: 'relative' }}>
            {/* Connecting line */}
            <div style={{ position: 'absolute', top: '12px', left: '10%', right: '10%', height: '2px', backgroundColor: 'var(--border-color)', zIndex: 0 }}></div>
            
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', zIndex: 1 }}>
              <div style={{ width: '24px', height: '24px', borderRadius: '50%', backgroundColor: '#10b981', border: '4px solid var(--surface-color)' }}></div>
              <span style={{ fontSize: '0.75rem', marginTop: '8px', fontWeight: 600, color: '#10b981' }}>Draft</span>
            </div>
            
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', zIndex: 1 }}>
              <div style={{ width: '24px', height: '24px', borderRadius: '50%', backgroundColor: '#3b82f6', border: '4px solid var(--surface-color)', boxShadow: '0 0 0 2px #3b82f6' }}></div>
              <span style={{ fontSize: '0.75rem', marginTop: '8px', fontWeight: 600, color: '#3b82f6' }}>Dispatched</span>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', zIndex: 1 }}>
              <div style={{ width: '24px', height: '24px', borderRadius: '50%', backgroundColor: '#e5e7eb', border: '4px solid var(--surface-color)' }}></div>
              <span style={{ fontSize: '0.75rem', marginTop: '8px', color: 'var(--text-secondary)', fontWeight: 500 }}>Completed</span>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', zIndex: 1 }}>
              <div style={{ width: '24px', height: '24px', borderRadius: '50%', backgroundColor: '#e5e7eb', border: '4px solid var(--surface-color)' }}></div>
              <span style={{ fontSize: '0.75rem', marginTop: '8px', color: 'var(--text-secondary)', fontWeight: 500 }}>Cancelled</span>
            </div>
          </div>
        </div>

        {/* Create Trip Form */}
        <div className="panel" style={{ padding: '32px' }}>
          <h3 style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', textTransform: 'uppercase', marginBottom: '24px' }}>CREATE TR</h3>
          
          <div className="form-group">
            <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Source</label>
            <input type="text" className="form-control" value={source} onChange={e => setSource(e.target.value)} style={{ padding: '12px 20px' }} />
          </div>

          <div className="form-group">
            <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Destination</label>
            <input type="text" className="form-control" value={destination} onChange={e => setDestination(e.target.value)} style={{ padding: '12px 20px' }} />
          </div>

          <div className="form-group">
            <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Vehicle (Available Only)</label>
            <select className="form-control" value={vehicle} onChange={e => setVehicle(e.target.value)} style={{ padding: '12px 20px' }}>
              <option>VAN-05 - 500 kg capacity</option>
              <option>TRUCK-11 - 5 Ton capacity</option>
            </select>
          </div>

          <div className="form-group">
            <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Driver (Available Only)</label>
            <select className="form-control" value={driver} onChange={e => setDriver(e.target.value)} style={{ padding: '12px 20px' }}>
              <option>Alex</option>
              <option>Suresh</option>
            </select>
          </div>

          <div className="form-group">
            <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Cargo Weight (kg)</label>
            <input type="number" className="form-control" value={weight} onChange={e => setWeight(e.target.value)} style={{ padding: '12px 20px' }} />
          </div>

          <div className="form-group" style={{ marginBottom: '24px' }}>
            <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Planned Distance (km)</label>
            <input type="number" className="form-control" value={distance} onChange={e => setDistance(e.target.value)} style={{ padding: '12px 20px' }} />
          </div>

          {/* Validation Alert */}
          {isOverweight && (
            <div className="alert alert-error" style={{ flexDirection: 'column', alignItems: 'flex-start', borderRadius: '16px', padding: '16px 20px' }}>
              <div style={{ fontSize: '0.85rem', fontWeight: 500 }}>Vehicle Capacity: {capacity} kg</div>
              <div style={{ fontSize: '0.85rem', fontWeight: 500 }}>Cargo Weight: {weightNum} kg</div>
              <div style={{ fontSize: '0.85rem', fontWeight: 700, marginTop: '8px', display: 'flex', alignItems: 'center', gap: '8px' }}>
                <span style={{ fontSize: '1rem' }}>❌</span> Capacity exceeded by {weightNum - capacity} kg — dispatch blocked
              </div>
            </div>
          )}

          <div style={{ display: 'flex', gap: '16px', marginTop: isOverweight ? '0' : '24px' }}>
            <button className="btn btn-primary" disabled={isOverweight} style={{ opacity: isOverweight ? 0.5 : 1, cursor: isOverweight ? 'not-allowed' : 'pointer', flex: 1 }}>
              Dispatch {isOverweight && '(disabled)'}
            </button>
            <button className="btn" style={{ flex: 1, backgroundColor: 'var(--input-bg)', border: '1px solid var(--border-color)', color: 'var(--error-color)' }}>
              Cancel
            </button>
          </div>
        </div>
      </div>

      {/* RIGHT COLUMN: LIVE BOARD */}
      <div className="right-col">
        <h3 style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', textTransform: 'uppercase', marginBottom: '24px' }}>LIVE BOARD</h3>
        
        <div className="live-board-list" style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          {MOCK_LIVE_BOARD.map(trip => (
            <div key={trip.id} className="panel" style={{ padding: '24px', border: '1px solid var(--border-color)', boxShadow: '0 2px 8px rgba(0,0,0,0.02)' }}>
              <div className="flex-between" style={{ marginBottom: '16px' }}>
                <span style={{ fontWeight: 600, color: 'var(--text-secondary)', fontSize: '0.85rem' }}>{trip.id}</span>
                <span style={{ fontWeight: 600, fontSize: '0.85rem' }}>
                  {trip.vehicle} {trip.driver && <span style={{ color: 'var(--text-secondary)' }}>/ {trip.driver}</span>}
                </span>
              </div>
              <div style={{ fontSize: '1.1rem', fontWeight: 700, marginBottom: '24px' }}>
                {trip.source} <span style={{ color: 'var(--text-secondary)', margin: '0 8px', fontWeight: 400 }}>→</span> {trip.dest}
              </div>
              <div className="flex-between">
                <span className={`status-badge ${getStatusBadge(trip.status)}`}>{trip.status}</span>
                <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', fontWeight: 500 }}>{trip.meta}</span>
              </div>
            </div>
          ))}
        </div>

        <div style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', fontWeight: 500, marginTop: '24px', paddingLeft: '8px' }}>
          On Complete: odometer → Fuel log → expenses → Vehicle & Driver Available
        </div>
      </div>

    </div>
  );
};

export default Trips;
