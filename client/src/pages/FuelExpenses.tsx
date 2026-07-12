import React from 'react';

const MOCK_FUEL_LOGS = [
  { id: '1', vehicle: 'VAN-05', date: '05 Jul 2026', liters: '42 L', cost: '3,150' },
  { id: '2', vehicle: 'TRUCK-11', date: '06 Jul 2026', liters: '110 L', cost: '8,400' },
  { id: '3', vehicle: 'MINI-08', date: '06 Jul 2026', liters: '28 L', cost: '2,050' },
];

const MOCK_OTHER_EXPENSES = [
  { id: '1', trip: 'TR001', vehicle: 'VAN-05', toll: '120', other: '0', maint: '0', total: '120' },
  { id: '2', trip: 'TR001', vehicle: 'TRK-12', toll: '340', other: '150', maint: '18,000', total: '18,490' },
];

const FuelExpenses = () => {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '32px' }}>
      
      {/* FUEL LOGS */}
      <div className="panel" style={{ padding: '0', overflow: 'hidden' }}>
        <div style={{ padding: '24px 32px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '1px solid var(--border-color)' }}>
          <h3 style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', textTransform: 'uppercase', margin: 0 }}>FUEL LOGS</h3>
          <div style={{ display: 'flex', gap: '16px' }}>
            <button className="btn btn-primary" style={{ backgroundColor: '#d97706', borderColor: '#d97706', width: 'auto', padding: '8px 24px', marginTop: 0 }}>+ Log Fuel</button>
            <button className="btn btn-primary" style={{ backgroundColor: '#d97706', borderColor: '#d97706', width: 'auto', padding: '8px 24px', marginTop: 0 }}>+ Add Expense</button>
          </div>
        </div>
        <div className="table-responsive">
          <table className="clean-table" style={{ margin: 0 }}>
            <thead>
              <tr>
                <th style={{ paddingLeft: '32px' }}>VEHICLE</th>
                <th>DATE</th>
                <th>LITERS</th>
                <th>FUEL COST</th>
              </tr>
            </thead>
            <tbody>
              {MOCK_FUEL_LOGS.map(log => (
                <tr key={log.id}>
                  <td style={{ paddingLeft: '32px', fontWeight: 600 }}>{log.vehicle}</td>
                  <td>{log.date}</td>
                  <td>{log.liters}</td>
                  <td style={{ fontWeight: 600 }}>{log.cost}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* OTHER EXPENSES */}
      <div className="panel" style={{ padding: '0', overflow: 'hidden' }}>
        <div style={{ padding: '24px 32px', borderBottom: '1px solid var(--border-color)' }}>
          <h3 style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', textTransform: 'uppercase', margin: 0 }}>OTHER EXPENSES (TOLL / MISC)</h3>
        </div>
        <div className="table-responsive">
          <table className="clean-table" style={{ margin: 0 }}>
            <thead>
              <tr>
                <th style={{ paddingLeft: '32px' }}>TRIP</th>
                <th>VEHICLE</th>
                <th>TOLL</th>
                <th>OTHER</th>
                <th>MAINT. (LINKED)</th>
                <th>TOTAL</th>
              </tr>
            </thead>
            <tbody>
              {MOCK_OTHER_EXPENSES.map(exp => (
                <tr key={exp.id}>
                  <td style={{ paddingLeft: '32px', fontWeight: 600 }}>{exp.trip}</td>
                  <td>{exp.vehicle}</td>
                  <td>{exp.toll}</td>
                  <td>{exp.other}</td>
                  <td>{exp.maint}</td>
                  <td>
                    {/* The wireframe accidentally reused "Available/Completed" status badges here. 
                        We'll use the green badge style but place the correct total number inside to respect the visual structure. */}
                    <span className="status-badge badge-green" style={{ display: 'inline-block', minWidth: '80px', textAlign: 'center' }}>
                      {exp.total}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* FOOTER TOTAL */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '8px 8px 32px 8px' }}>
        <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', textTransform: 'uppercase', fontWeight: 600 }}>
          TOTAL OPERATIONAL COST (AUTO) = FUEL + MAINT
        </span>
        <span style={{ fontSize: '1.25rem', color: '#d97706', fontWeight: 700 }}>
          34,070
        </span>
      </div>

    </div>
  );
};

export default FuelExpenses;
