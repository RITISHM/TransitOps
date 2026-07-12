import React, { useState } from 'react';

const INITIAL_TRIPS = [
  { id: 'TR001', vehicle: 'VAN-05', driver: 'ALEX', source: 'Gandhinagar Depot', dest: 'Ahmedabad Hub', status: 'Dispatched', meta: '45 min' },
  { id: 'TR004', vehicle: 'TRUCK-04', driver: 'SURESH', source: 'Vatva Industrial Area', dest: 'Sanand Warehouse', status: 'Draft', meta: 'Awaiting driver' },
  { id: 'TR006', vehicle: 'Unassigned', driver: '', source: 'Mansa', dest: 'Kalol Depot', status: 'Cancelled', meta: 'Vehicle went to shop' },
];

const Trips = () => {
  const [trips, setTrips] = useState(INITIAL_TRIPS);
  const [source, setSource] = useState('Gandhinagar Depot');
  const [destination, setDestination] = useState('Ahmedabad Hub');
  const [vehicle, setVehicle] = useState('VAN-05 - 500 kg capacity');
  const [driver, setDriver] = useState('Alex');
  const [weight, setWeight] = useState('400');
  const [distance, setDistance] = useState('38');

  // Completion Modal State
  const [completingTrip, setCompletingTrip] = useState<any>(null);
  const [odometer, setOdometer] = useState('');
  const [fuelVol, setFuelVol] = useState('');
  const [fuelCost, setFuelCost] = useState('');
  const [expenses, setExpenses] = useState('');

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

  const handleCompleteSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (completingTrip) {
      setTrips(trips.map(t => 
        t.id === completingTrip.id 
          ? { ...t, status: 'Completed', meta: 'Vehicle & Driver Available' } 
          : t
      ));
      setCompletingTrip(null);
      // Reset form
      setOdometer('');
      setFuelVol('');
      setFuelCost('');
      setExpenses('');
    }
  };

  return (
    <>
      {/* Modal Overlay */}
      {completingTrip && (
        <div style={{
          position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
          backgroundColor: 'rgba(0,0,0,0.4)', zIndex: 1000,
          backdropFilter: 'blur(8px)', WebkitBackdropFilter: 'blur(8px)',
          display: 'flex', alignItems: 'center', justifyContent: 'center'
        }}>
          <div className="panel" style={{ width: '100%', maxWidth: '420px', padding: '32px' }}>
            <h2 style={{ fontSize: '1.25rem', marginBottom: '8px' }}>Complete Trip {completingTrip.id}</h2>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.85rem', marginBottom: '24px' }}>
              Enter final trip logs to release {completingTrip.vehicle} and driver {completingTrip.driver}.
            </p>
            
            <form onSubmit={handleCompleteSubmit}>
              <div className="form-group">
                <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Final Odometer (km)</label>
                <input required type="number" className="form-control" value={odometer} onChange={e => setOdometer(e.target.value)} style={{ padding: '10px 16px' }} />
              </div>
              <div style={{ display: 'flex', gap: '16px' }}>
                <div className="form-group" style={{ flex: 1 }}>
                  <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Fuel (Liters)</label>
                  <input type="number" className="form-control" value={fuelVol} onChange={e => setFuelVol(e.target.value)} style={{ padding: '10px 16px' }} />
                </div>
                <div className="form-group" style={{ flex: 1 }}>
                  <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Fuel Cost ($)</label>
                  <input type="number" className="form-control" value={fuelCost} onChange={e => setFuelCost(e.target.value)} style={{ padding: '10px 16px' }} />
                </div>
              </div>
              <div className="form-group" style={{ marginBottom: '32px' }}>
                <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Additional Expenses ($)</label>
                <input type="number" className="form-control" value={expenses} onChange={e => setExpenses(e.target.value)} style={{ padding: '10px 16px' }} />
              </div>

              <div style={{ display: 'flex', gap: '12px' }}>
                <button type="submit" className="btn btn-primary" style={{ flex: 1, marginTop: 0 }}>Submit & Complete</button>
                <button type="button" className="btn" onClick={() => setCompletingTrip(null)} style={{ flex: 1, marginTop: 0, backgroundColor: 'var(--input-bg)', border: '1px solid var(--border-color)' }}>Cancel</button>
              </div>
            </form>
          </div>
        </div>
      )}

      <div className="trips-container" style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '32px' }}>
        
        {/* LEFT COLUMN: CREATE TRIP & LIFECYCLE */}
        <div className="left-col" style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          {/* Trip Lifecycle Stepper */}
          <div className="panel" style={{ padding: '24px' }}>
            <h3 style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', textTransform: 'uppercase', marginBottom: '0' }}>TRIP LIFECYCLE</h3>
            <div className="stepper" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '24px', position: 'relative' }}>
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
              <button className="btn btn-primary" disabled={isOverweight} style={{ opacity: isOverweight ? 0.5 : 1, cursor: isOverweight ? 'not-allowed' : 'pointer', flex: 1, marginTop: 0 }}>
                Dispatch {isOverweight && '(disabled)'}
              </button>
              <button className="btn" style={{ flex: 1, backgroundColor: 'var(--input-bg)', border: '1px solid var(--border-color)', color: 'var(--error-color)', marginTop: 0 }}>
                Cancel
              </button>
            </div>
          </div>
        </div>

        {/* RIGHT COLUMN: LIVE BOARD */}
        <div className="right-col">
          <h3 style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', textTransform: 'uppercase', marginBottom: '24px' }}>LIVE BOARD</h3>
          
          <div className="live-board-list" style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            {trips.map(trip => (
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
                  {trip.status === 'Dispatched' ? (
                    <button 
                      onClick={() => setCompletingTrip(trip)}
                      className="btn btn-primary" 
                      style={{ padding: '6px 16px', fontSize: '0.75rem', width: 'auto', marginTop: 0 }}
                    >
                      Mark Complete
                    </button>
                  ) : (
                    <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', fontWeight: 500 }}>{trip.meta}</span>
                  )}
                </div>
              </div>
            ))}
          </div>

        </div>

      </div>
    </>
  );
};

export default Trips;
