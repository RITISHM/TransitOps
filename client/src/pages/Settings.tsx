import React, { useState } from 'react';

const Settings = () => {
  const [depotName, setDepotName] = useState('Gandhinagar Depot GJ4');
  const [currency, setCurrency] = useState('INR (Rs)');
  const [distanceUnit, setDistanceUnit] = useState('Kilometers');

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    alert('Settings saved!');
  };

  return (
    <div style={{ maxWidth: '600px', margin: '0 auto', display: 'flex', flexDirection: 'column', gap: '32px' }}>
      
      {/* GENERAL SETTINGS */}
      <div className="panel" style={{ padding: '32px' }}>
        <h3 style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', textTransform: 'uppercase', marginBottom: '24px' }}>GENERAL SETTINGS</h3>
        
        <form onSubmit={handleSave}>
          <div className="form-group">
            <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Depot Name</label>
            <input type="text" className="form-control" value={depotName} onChange={e => setDepotName(e.target.value)} style={{ padding: '12px 20px' }} />
          </div>

          <div className="form-group">
            <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Currency</label>
            <input type="text" className="form-control" value={currency} onChange={e => setCurrency(e.target.value)} style={{ padding: '12px 20px' }} />
          </div>

          <div className="form-group" style={{ marginBottom: '32px' }}>
            <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>Distance Unit</label>
            <input type="text" className="form-control" value={distanceUnit} onChange={e => setDistanceUnit(e.target.value)} style={{ padding: '12px 20px' }} />
          </div>

          <button type="submit" className="btn btn-primary" style={{ width: 'auto', padding: '12px 32px', marginTop: 0 }}>
            Save changes
          </button>
        </form>
      </div>

    </div>
  );
};

export default Settings;
